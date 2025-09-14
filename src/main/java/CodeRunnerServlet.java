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
    private static final String CPP_INTERPRETER = "CSharpInterpreter.exe";
    
    // Get the absolute path to the C++ interpreter
    private String getCppInterpreterPath() {
        // Use the known absolute path first
        String absolutePath = "C:\\Users\\LENOVO\\Documents\\C# Cheatsheet\\CSharpInterpreter.exe";
        File absoluteFile = new File(absolutePath);
        if (absoluteFile.exists()) {
            return absolutePath;
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
        
        return absolutePath; // Return the expected absolute path even if not found
    }
    
    // C++ interpreter only - no Java fallback
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            printCodeRunnerPage(out);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String code = request.getParameter("csharp_code");
        String action = request.getParameter("action");
        
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            if ("run_code".equals(action) && code != null && !code.trim().isEmpty()) {
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
                String expectedPath = "C:\\Users\\LENOVO\\Documents\\CSharp Cheatsheet\\CSharpInterpreter.exe";
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
            String workingDir = "C:\\Users\\LENOVO\\Documents\\CSharp Cheatsheet";
            
            // Write code to temporary file in the working directory
            File tempCodeFile = new File(workingDir, TEMP_CODE_FILE);
            File tempOutputFile = new File(workingDir, TEMP_OUTPUT_FILE);
            
            try (FileWriter writer = new FileWriter(tempCodeFile)) {
                writer.write(code);
            }
            
            // Execute C++ interpreter with file input
            ProcessBuilder pb = new ProcessBuilder(interpreterPath, tempCodeFile.getAbsolutePath(), tempOutputFile.getAbsolutePath());
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
                    } catch (Exception e) {
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
            
        } catch (Exception e) {
            // Clean up files
            try {
                new File("C:\\Users\\LENOVO\\Documents\\C# Cheatsheet", TEMP_CODE_FILE).delete();
                new File("C:\\Users\\LENOVO\\Documents\\C# Cheatsheet", TEMP_OUTPUT_FILE).delete();
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
    
    private void printCodeRunnerPage(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("    <title>C# Code Runner | Live Interpreter</title>");
        out.println("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">");
        out.println("    <link href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css\" rel=\"stylesheet\">");
        out.println("    <link href=\"css/styles.css\" rel=\"stylesheet\">");
        out.println("    <style>");
        out.println("        .code-editor { font-family: 'Consolas', monospace; background: #1e1e1e; color: #d4d4d4; border: none; resize: vertical; }");
        out.println("        .output-area { background: #0d1117; color: #f0f6fc; font-family: 'Consolas', monospace; border: none; min-height: 200px; }");
        out.println("        .run-btn { background: linear-gradient(45deg, #28a745, #20c997); border: none; }");
        out.println("        .copy-code-btn { position: absolute; top: 10px; right: 10px; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        
        // Header
        out.println("    <header class=\"header\">");
        out.println("        <div class=\"container\">");
        out.println("            <h1><i class=\"fas fa-play-circle\"></i> C# Code Runner</h1>");
        out.println("            <p>Write, run, and test C# code instantly with our live interpreter</p>");
        out.println("        </div>");
        out.println("    </header>");
        
        // Navigation
        out.println("    <nav class=\"navigation\">");
        out.println("        <div class=\"container\">");
        out.println("            <div class=\"nav-menu\">");
        out.println("                <a href=\"index.html\" class=\"nav-item\">Home</a>");
        out.println("                <a href=\"CheatsheetServlet\" class=\"nav-item\">C# Cheatsheet</a>");
        out.println("                <a href=\"CodeRunnerServlet\" class=\"nav-item active\">Code Runner</a>");
        out.println("                <a href=\"#examples\" class=\"nav-item\">Examples</a>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("    </nav>");
        
        // Main Content
        out.println("    <main class=\"main-content\">");
        out.println("        <div class=\"container\">");
        
        // Code Editor Section
        out.println("            <section class=\"mb-5\">");
        out.println("                <div class=\"card\">");
        out.println("                    <div class=\"card-header d-flex justify-content-between align-items-center\">");
        out.println("                        <h3><i class=\"fas fa-code\"></i> C# Code Editor</h3>");
        out.println("                        <div>");
        out.println("                            <button id=\"runBtn\" class=\"btn run-btn text-white me-2\">");
        out.println("                                <i class=\"fas fa-play\"></i> Run Code");
        out.println("                            </button>");
        out.println("                            <button id=\"clearBtn\" class=\"btn btn-secondary\">");
        out.println("                                <i class=\"fas fa-trash\"></i> Clear");
        out.println("                            </button>");
        out.println("                        </div>");
        out.println("                    </div>");
        out.println("                    <div class=\"card-body\">");
        out.println("                        <div class=\"position-relative\">");
        out.println("                            <textarea id=\"codeEditor\" class=\"form-control code-editor\" rows=\"15\" placeholder=\"Write your C# code here...\">");
        
        // Default example code
        out.println("// Welcome to C# Code Runner!");
        out.println("// Try running this example code:");
        out.println("");
        out.println("int x = 10;");
        out.println("int y = 25;");
        out.println("int sum = x + y;");
        out.println("");
        out.println("Console.WriteLine(\"Hello, C# World!\");");
        out.println("Console.WriteLine($\"The sum of {x} and {y} is {sum}\");");
        out.println("");
        out.println("string name = \"Developer\";");
        out.println("Console.WriteLine($\"Welcome, {name}!\");");
        
        out.println("                            </textarea>");
        out.println("                            <button class=\"btn btn-sm btn-outline-light copy-code-btn\" onclick=\"copyCode()\">");
        out.println("                                <i class=\"fas fa-copy\"></i> Copy");
        out.println("                            </button>");
        out.println("                        </div>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </section>");
        
        // Output Section
        out.println("            <section class=\"mb-5\">");
        out.println("                <div class=\"card\">");
        out.println("                    <div class=\"card-header d-flex justify-content-between align-items-center\">");
        out.println("                        <h3><i class=\"fas fa-terminal\"></i> Output</h3>");
        out.println("                        <button id=\"copyOutputBtn\" class=\"btn btn-sm btn-outline-primary\" onclick=\"copyOutput()\">");
        out.println("                            <i class=\"fas fa-copy\"></i> Copy Output");
        out.println("                        </button>");
        out.println("                    </div>");
        out.println("                    <div class=\"card-body\">");
        out.println("                        <textarea id=\"outputArea\" class=\"form-control output-area\" rows=\"8\" readonly placeholder=\"Code output will appear here...\"></textarea>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </section>");
        
        // Examples Section
        printExamplesSection(out);
        
        out.println("        </div>");
        out.println("    </main>");
        
        // Footer
        out.println("    <footer class=\"footer\">");
        out.println("        <div class=\"container text-center\">");
        out.println("            <p>&copy; 2024 C# Code Runner. Powered by C++ Interpreter</p>");
        out.println("        </div>");
        out.println("    </footer>");
        
        // JavaScript
        out.println("    <script>");
        out.println("        const runBtn = document.getElementById('runBtn');");
        out.println("        const clearBtn = document.getElementById('clearBtn');");
        out.println("        const codeEditor = document.getElementById('codeEditor');");
        out.println("        const outputArea = document.getElementById('outputArea');");
        out.println("");
        out.println("        runBtn.addEventListener('click', runCode);");
        out.println("        clearBtn.addEventListener('click', clearCode);");
        out.println("");
        out.println("        function runCode() {");
        out.println("            const code = codeEditor.value.trim();");
        out.println("            if (!code) {");
        out.println("                outputArea.value = 'Please enter some C# code to run.';");
        out.println("                return;");
        out.println("            }");
        out.println("");
        out.println("            runBtn.disabled = true;");
        out.println("            runBtn.innerHTML = '<i class=\"fas fa-spinner fa-spin\"></i> Running...';");
        out.println("            outputArea.value = 'Executing code...';");
        out.println("");
        out.println("            fetch('CodeRunnerServlet', {");
        out.println("                method: 'POST',");
        out.println("                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },");
        out.println("                body: 'action=run_code&csharp_code=' + encodeURIComponent(code)");
        out.println("            })");
        out.println("            .then(response => response.json())");
        out.println("            .then(data => {");
        out.println("                if (data.success) {");
        out.println("                    outputArea.value = data.output || 'Code executed successfully (no output)';");
        out.println("                } else {");
        out.println("                    outputArea.value = 'Error: ' + (data.error || 'Unknown error occurred');");
        out.println("                }");
        out.println("            })");
        out.println("            .catch(error => {");
        out.println("                outputArea.value = 'Network error: ' + error.message;");
        out.println("            })");
        out.println("            .finally(() => {");
        out.println("                runBtn.disabled = false;");
        out.println("                runBtn.innerHTML = '<i class=\"fas fa-play\"></i> Run Code';");
        out.println("            });");
        out.println("        }");
        out.println("");
        out.println("        function clearCode() {");
        out.println("            codeEditor.value = '';");
        out.println("            outputArea.value = '';");
        out.println("        }");
        out.println("");
        out.println("        function copyCode() {");
        out.println("            codeEditor.select();");
        out.println("            document.execCommand('copy');");
        out.println("            showToast('Code copied to clipboard!');");
        out.println("        }");
        out.println("");
        out.println("        function copyOutput() {");
        out.println("            outputArea.select();");
        out.println("            document.execCommand('copy');");
        out.println("            showToast('Output copied to clipboard!');");
        out.println("        }");
        out.println("");
        out.println("        function loadExample(code) {");
        out.println("            codeEditor.value = code;");
        out.println("            showToast('Example loaded!');");
        out.println("        }");
        out.println("");
        out.println("        function showToast(message) {");
        out.println("            // Simple toast notification");
        out.println("            const toast = document.createElement('div');");
        out.println("            toast.style.cssText = 'position:fixed;top:20px;right:20px;background:#28a745;color:white;padding:12px 20px;border-radius:5px;z-index:9999;';");
        out.println("            toast.textContent = message;");
        out.println("            document.body.appendChild(toast);");
        out.println("            setTimeout(() => toast.remove(), 3000);");
        out.println("        }");
        out.println("    </script>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void printExamplesSection(PrintWriter out) {
        out.println("            <section id=\"examples\" class=\"mb-5\">");
        out.println("                <div class=\"card\">");
        out.println("                    <div class=\"card-header\">");
        out.println("                        <h3><i class=\"fas fa-lightbulb\"></i> Example Code Snippets</h3>");
        out.println("                    </div>");
        out.println("                    <div class=\"card-body\">");
        out.println("                        <div class=\"row\">");
        
        // Example 1: Variables
        out.println("                            <div class=\"col-md-6 mb-3\">");
        out.println("                                <div class=\"card h-100\">");
        out.println("                                    <div class=\"card-body\">");
        out.println("                                        <h5>Variables & Math</h5>");
        out.println("                                        <div class=\"code-block\">");
        out.println("                                            <pre>int a = 15;");
        out.println("int b = 4;");
        out.println("Console.WriteLine($\"Sum: {a + b}\");");
        out.println("Console.WriteLine($\"Product: {a * b}\");");
        out.println("Console.WriteLine($\"Division: {a / b}\");</pre>");
        out.println("                                        </div>");
        out.println("                                        <button class=\"btn btn-sm btn-primary\" onclick=\"loadExample('int a = 15;\\nint b = 4;\\nConsole.WriteLine($\\\"Sum: {a + b}\\\");\\nConsole.WriteLine($\\\"Product: {a * b}\\\");\\nConsole.WriteLine($\\\"Division: {a / b}\\\");')\">Load Example</button>");
        out.println("                                    </div>");
        out.println("                                </div>");
        out.println("                            </div>");
        
        // Example 2: Strings
        out.println("                            <div class=\"col-md-6 mb-3\">");
        out.println("                                <div class=\"card h-100\">");
        out.println("                                    <div class=\"card-body\">");
        out.println("                                        <h5>String Operations</h5>");
        out.println("                                        <div class=\"code-block\">");
        out.println("                                            <pre>string firstName = \"John\";");
        out.println("string lastName = \"Doe\";");
        out.println("string fullName = firstName + \" \" + lastName;");
        out.println("Console.WriteLine($\"Hello, {fullName}!\");");
        out.println("Console.WriteLine($\"Length: {fullName.Length}\");</pre>");
        out.println("                                        </div>");
        out.println("                                        <button class=\"btn btn-sm btn-primary\" onclick=\"loadExample('string firstName = \\\"John\\\";\\nstring lastName = \\\"Doe\\\";\\nstring fullName = firstName + \\\" \\\" + lastName;\\nConsole.WriteLine($\\\"Hello, {fullName}!\\\");\\nConsole.WriteLine($\\\"Length: {fullName.Length}\\\");')\">Load Example</button>");
        out.println("                                    </div>");
        out.println("                                </div>");
        out.println("                            </div>");
        
        out.println("                        </div>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </section>");
    }
}