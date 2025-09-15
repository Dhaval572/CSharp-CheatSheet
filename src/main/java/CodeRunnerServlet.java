import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CodeRunnerServlet")
public class CodeRunnerServlet extends HttpServlet {
    
    private static final String TEMP_CODE_FILE = "temp_code.cs";
    private static final String TEMP_OUTPUT_FILE = "temp_output.txt";
    // private static final String CPP_INTERPRETER = "CSharpInterpreter.exe";
    
    // Get the absolute path to the C++ interpreter
    private String getCppInterpreterPath() {
        // Use the known absolute path first
        String interpreterPath = "CSharpInterpreter.exe";
        File absoluteFile = new File(interpreterPath);
        if (absoluteFile.exists()) {
            return interpreterPath;
        }
        
        // Try current directory as fallback
        File currentDir = new File("CSharpInterpreter.exe");
        if (currentDir.exists()) {
            return currentDir.getAbsolutePath();
        }
        
        // Try the web application root directory
        String webappRoot = getServletContext().getRealPath("/");
        if (webappRoot != null) {
            File webappInterpreter = new File(webappRoot, "CSharpInterpreter.exe");
            if (webappInterpreter.exists()) {
                return webappInterpreter.getAbsolutePath();
            }
            
            // Try going up to project root
            File projectInterpreter = new File(webappRoot, "../../../CSharpInterpreter.exe");
            if (projectInterpreter.exists()) {
                return projectInterpreter.getAbsolutePath();
            }
        }
        
        // Try looking in the project root relative to webapp
        try {
            File projectRoot = new File(System.getProperty("user.dir"));
            File interpreter = new File(projectRoot, "CSharpInterpreter.exe");
            if (interpreter.exists()) {
                return interpreter.getAbsolutePath();
            }
        } catch (Exception e) {
            // Ignore and continue
        }
        
        return interpreterPath; // Return the expected absolute path even if not found
    }
    
    // C++ interpreter only - no Java fallback
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Forward to JSP page instead of generating HTML
        request.getRequestDispatcher("jsp/coderunner.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String code = request.getParameter("csharp_code");
        String action = request.getParameter("action");
        
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            if (
                "run_code".equals(action) 
                && code != null 
                && !code.trim().isEmpty()
            ) {
                String result = executeCode(code);
                out.println("{\"success\": true, \"output\": \"" + escapeJson(result) + "\"}");
            } else {
                out.println("{\"success\": false, \"error\": \"No code provided\"}");
            }
        }
    }
    
    private String executeCode(String code) {
        try {
            // Use C++ interpreter only
            if (isCppInterpreterAvailable()) {
                return executeCppInterpreter(code);
            } else {
                String expectedPath = "CSharpInterpreter.exe";
                return "Error: C++ interpreter not found at expected location: " + expectedPath + "\n" +
                       "Please build it first using: g++ -std=c++17 -O2 -o CSharpInterpreter.exe src/main/cpp/CSharpInterpreter.cpp";
            }
        } catch (Exception e) {
            return "Error executing code: " + e.getMessage();
        }
    }
    
    private boolean isCppInterpreterAvailable() {
        String interpreterPath = getCppInterpreterPath();
        File interpreter = new File(interpreterPath);
        return interpreter.exists() && interpreter.canExecute();
    }
    
    private String executeCppInterpreter(String code) {
        try {
            // Get the correct interpreter path
            String interpreterPath = getCppInterpreterPath();
            
            // Use the project directory as working directory
            String workingDir = System.getProperty("user.dir");
            
            // Write code to temporary file in the working directory
            File tempCodeFile = new File(workingDir, TEMP_CODE_FILE);
            File tempOutputFile = new File(workingDir, TEMP_OUTPUT_FILE);
            
            try (FileWriter writer = new FileWriter(tempCodeFile)) {
                writer.write(code);
            }
            
            // Execute C++ interpreter with file input
            ProcessBuilder pb = 
            new ProcessBuilder(
                interpreterPath, 
                tempCodeFile.getAbsolutePath(), 
                tempOutputFile.getAbsolutePath()
            );

            pb.directory(new File(workingDir));
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            // Read console output (for errors)
            StringBuilder consoleOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    consoleOutput.append(line).append("\n");
                }
            }
            
            // Wait for process to complete
            int exitCode = process.waitFor();
            
            // Clean up input file
            tempCodeFile.delete();
            
            if (exitCode == 0) {
                // Read output from file if it exists
                if (tempOutputFile.exists()) {
                    try {
                        String result = Files.readString(tempOutputFile.toPath()).trim();
                        tempOutputFile.delete();
                        return result.isEmpty() ? "Code executed successfully (no output)" : "[C++ Interpreter]\n" + result;
                    } catch (IOException e) {
                        tempOutputFile.delete();
                        return "[C++ Interpreter]\n" + consoleOutput.toString().trim();
                    }
                } else {
                    String output = consoleOutput.toString().trim();
                    return output.isEmpty() ? "Code executed successfully (no output)" : "[C++ Interpreter]\n" + output;
                }
            } else {
                // Clean up output file if exists
                tempOutputFile.delete();
                return "C++ Execution failed with exit code: " + exitCode + "\nInterpreter path: " + interpreterPath + "\n" + consoleOutput.toString();
            }
            
        } catch (IOException | InterruptedException e) {
            // Clean up files
            try {
                
                new File(System.getProperty("user.dir"), TEMP_CODE_FILE).delete();
                new File(System.getProperty("user.dir"), TEMP_OUTPUT_FILE).delete();
            } catch (Exception cleanupEx) {
                // Ignore cleanup errors
            }
            return "Error with C++ interpreter: " + e.getMessage() + "\nInterpreter path: " + getCppInterpreterPath();
        }
    }
    

    

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\r", "\\r")
                   .replace("\n", "\\n")
                   .replace("\t", "\\t");
    }
}