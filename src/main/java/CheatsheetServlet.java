import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CheatsheetServlet")
public class CheatsheetServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("    <meta charset=\"UTF-8\">");
            out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("    <title>C# Complete Cheatsheet</title>");
            out.println("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">");
            out.println("    <link href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css\" rel=\"stylesheet\">");
            out.println("    <link href=\"css/styles.css\" rel=\"stylesheet\">");
            out.println("</head>");
            out.println("<body>");
            
            // Header
            out.println("    <header class=\"header\">");
            out.println("        <div class=\"container\">");
            out.println("            <h1><i class=\"fab fa-microsoft\"></i> C# Complete Cheatsheet</h1>");
            out.println("            <p>Your comprehensive guide to C# programming language</p>");
            out.println("        </div>");
            out.println("    </header>");
            
            // Navigation
            out.println("    <nav class=\"navigation\">");
            out.println("        <div class=\"container\">");
            out.println("            <div class=\"nav-menu\">");
            out.println("                <a href=\"#variables\" class=\"nav-item\">Variables</a>");
            out.println("                <a href=\"#operators\" class=\"nav-item\">Operators</a>");
            out.println("                <a href=\"#control-flow\" class=\"nav-item\">Control Flow</a>");
            out.println("                <a href=\"#methods\" class=\"nav-item\">Methods</a>");
            out.println("                <a href=\"#oop\" class=\"nav-item\">OOP</a>");
            out.println("                <a href=\"#collections\" class=\"nav-item\">Collections</a>");
            out.println("                <a href=\"CodeRunnerServlet\" class=\"nav-item\">Try Code</a>");
            out.println("            </div>");
            out.println("        </div>");
            out.println("    </nav>");
            
            // Main Content
            out.println("    <main class=\"main-content\">");
            out.println("        <div class=\"container\">");
            
            // Variables Section
            printVariablesSection(out);
            
            // Operators Section  
            printOperatorsSection(out);
            
            // Control Flow Section
            printControlFlowSection(out);
            
            // Methods Section
            printMethodsSection(out);
            
            // OOP Section
            printOOPSection(out);
            
            // Collections Section
            printCollectionsSection(out);
            
            out.println("        </div>");
            out.println("    </main>");
            
            // Footer
            out.println("    <footer class=\"footer\">");
            out.println("        <div class=\"container text-center\">");
            out.println("            <p>&copy; 2024 C# Cheatsheet. Database: <strong>Csharpdb</strong></p>");
            out.println("        </div>");
            out.println("    </footer>");
            
            out.println("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js\"></script>");
            out.println("    <script>");
            out.println("        function copyCodeBlock(button) {");
            out.println("            const codeBlock = button.parentElement.querySelector('pre');");
            out.println("            const text = codeBlock.textContent;");
            out.println("            navigator.clipboard.writeText(text).then(() => {");
            out.println("                const originalText = button.innerHTML;");
            out.println("                button.innerHTML = '<i class=\\\"fas fa-check\\\"></i> Copied!';");
            out.println("                button.classList.remove('btn-outline-primary');");
            out.println("                button.classList.add('btn-success');");
            out.println("                setTimeout(() => {");
            out.println("                    button.innerHTML = originalText;");
            out.println("                    button.classList.remove('btn-success');");
            out.println("                    button.classList.add('btn-outline-primary');");
            out.println("                }, 2000);");
            out.println("            }).catch(err => {");
            out.println("                console.error('Failed to copy: ', err);");
            out.println("                button.innerHTML = '<i class=\\\"fas fa-times\\\"></i> Failed!';");
            out.println("            });");
            out.println("        }");
            out.println("    </script>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void printVariablesSection(PrintWriter out) {
        out.println("        <section id=\"variables\" class=\"mb-5\">");
        out.println("            <div class=\"card\">");
        out.println("                <div class=\"card-header\">");
        out.println("                    <h3><i class=\"fas fa-database\"></i> Variables & Data Types</h3>");
        out.println("                </div>");
        out.println("                <div class=\"card-body\">");
        out.println("                    <div class=\"row\">");
        out.println("                        <div class=\"col-md-6\">");
        out.println("                            <h5>Value Types</h5>");
        out.println("                            <div class=\"code-block\">");
        out.println("                                <pre>");
        out.println("int age = 25;");
        out.println("long population = 7800000000L;");
        out.println("float price = 19.99f;");
        out.println("double distance = 123.456;");
        out.println("decimal money = 99.99m;");
        out.println("bool isActive = true;");
        out.println("char grade = 'A';");
        out.println("                                </pre>");
        out.println("                            </div>");
        out.println("                        </div>");
        out.println("                        <div class=\"col-md-6\">");
        out.println("                            <h5>Reference Types</h5>");
        out.println("                            <div class=\"code-block\">");
        out.println("                                <pre>");
        out.println("string name = \"John Doe\";");
        out.println("string multiline = @\"Line 1");
        out.println("Line 2\";");
        out.println("string interpolated = $\"Hello {name}\";");
        out.println("");
        out.println("int? nullableAge = null;");
        out.println("var autoInt = 42;");
        out.println("const double PI = 3.14159;");
        out.println("                                </pre>");
        out.println("                            </div>");
        out.println("                        </div>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </section>");
    }
    
    private void printOperatorsSection(PrintWriter out) {
        out.println("        <section id=\"operators\" class=\"mb-5\">");
        out.println("            <div class=\"card\">");
        out.println("                <div class=\"card-header\">");
        out.println("                    <h3><i class=\"fas fa-calculator\"></i> Operators</h3>");
        out.println("                </div>");
        out.println("                <div class=\"card-body\">");
        out.println("                    <div class=\"code-block\">");
        out.println("                        <pre>");
        out.println("// Arithmetic: +, -, *, /, %");
        out.println("int sum = 10 + 5;   // 15");
        out.println("int diff = 10 - 5;  // 5");
        out.println("int product = 10 * 5; // 50");
        out.println("");
        out.println("// Comparison: ==, !=, >, <, >=, <=");
        out.println("bool equal = (a == b);");
        out.println("bool greater = (a > b);");
        out.println("");
        out.println("// Logical: &&, ||, !");
        out.println("bool result = (a > 5) && (b < 10);");
        out.println("");
        out.println("// Assignment: =, +=, -=, *=, /=");
        out.println("x += 5;  // x = x + 5");
        out.println("");
        out.println("// Null-coalescing: ??, ??=");
        out.println("string result = name ?? \"default\";");
        out.println("                        </pre>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </section>");
    }
    
    private void printControlFlowSection(PrintWriter out) {
        out.println("        <section id=\"control-flow\" class=\"mb-5\">");
        out.println("            <div class=\"card\">");
        out.println("                <div class=\"card-header\">");
        out.println("                    <h3><i class=\"fas fa-code-branch\"></i> Control Flow & Loops</h3>");
        out.println("                </div>");
        out.println("                <div class=\"card-body\">");
        out.println("                    <div class=\"row\">");
        out.println("                        <div class=\"col-md-6\">");
        out.println("                            <div class=\"code-block\">");
        out.println("                                <pre>");
        out.println("// If-else");
        out.println("if (score >= 90)");
        out.println("    grade = \"A\";");
        out.println("else if (score >= 80)");
        out.println("    grade = \"B\";");
        out.println("else");
        out.println("    grade = \"F\";");
        out.println("");
        out.println("// Ternary");
        out.println("string status = (score >= 60) ? \"Pass\" : \"Fail\";");
        out.println("");
        out.println("// Switch");
        out.println("switch (day)");
        out.println("{");
        out.println("    case 1: dayName = \"Monday\"; break;");
        out.println("    case 2: dayName = \"Tuesday\"; break;");
        out.println("    default: dayName = \"Unknown\"; break;");
        out.println("}");
        out.println("                                </pre>");
        out.println("                            </div>");
        out.println("                        </div>");
        out.println("                        <div class=\"col-md-6\">");
        out.println("                            <div class=\"code-block\">");
        out.println("                                <pre>");
        out.println("// For loop");
        out.println("for (int i = 0; i < 5; i++)");
        out.println("{");
        out.println("    Console.WriteLine(i);");
        out.println("}");
        out.println("");
        out.println("// While loop");
        out.println("while (count < 3)");
        out.println("{");
        out.println("    count++;");
        out.println("}");
        out.println("");
        out.println("// Foreach loop");
        out.println("foreach (string item in items)");
        out.println("{");
        out.println("    Console.WriteLine(item);");
        out.println("}");
        out.println("                                </pre>");
        out.println("                            </div>");
        out.println("                        </div>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </section>");
    }
    
    private void printMethodsSection(PrintWriter out) {
        out.println("        <section id=\"methods\" class=\"mb-5\">");
        out.println("            <div class=\"card\">");
        out.println("                <div class=\"card-header\">");
        out.println("                    <h3><i class=\"fas fa-function\"></i> Methods</h3>");
        out.println("                </div>");
        out.println("                <div class=\"card-body\">");
        out.println("                    <div class=\"code-block\">");
        out.println("                        <pre>");
        out.println("// Basic method");
        out.println("public static void SayHello()");
        out.println("{");
        out.println("    Console.WriteLine(\"Hello!\");");
        out.println("}");
        out.println("");
        out.println("// Method with parameters and return");
        out.println("public static int Add(int a, int b)");
        out.println("{");
        out.println("    return a + b;");
        out.println("}");
        out.println("");
        out.println("// Method overloading");
        out.println("public static double Add(double a, double b) => a + b;");
        out.println("");
        out.println("// Default parameters");
        out.println("public static void Greet(string name = \"World\")");
        out.println("{");
        out.println("    Console.WriteLine($\"Hello {name}!\");");
        out.println("}");
        out.println("                        </pre>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </section>");
    }
    
    private void printOOPSection(PrintWriter out) {
        out.println("        <section id=\"oop\" class=\"mb-5\">");
        out.println("            <div class=\"card\">");
        out.println("                <div class=\"card-header\">");
        out.println("                    <h3><i class=\"fas fa-cube\"></i> Object-Oriented Programming</h3>");
        out.println("                </div>");
        out.println("                <div class=\"card-body\">");
        out.println("                    <div class=\"code-block\">");
        out.println("                        <pre>");
        out.println("// Class definition");
        out.println("public class Person");
        out.println("{");
        out.println("    public string Name { get; set; }");
        out.println("    public int Age { get; set; }");
        out.println("");
        out.println("    public Person(string name, int age)");
        out.println("    {");
        out.println("        Name = name;");
        out.println("        Age = age;");
        out.println("    }");
        out.println("");
        out.println("    public virtual void Introduce()");
        out.println("    {");
        out.println("        Console.WriteLine($\"Hi, I'm {Name}\");");
        out.println("    }");
        out.println("}");
        out.println("");
        out.println("// Inheritance");
        out.println("public class Student : Person");
        out.println("{");
        out.println("    public string School { get; set; }");
        out.println("");
        out.println("    public Student(string name, int age, string school) ");
        out.println("        : base(name, age)");
        out.println("    {");
        out.println("        School = school;");
        out.println("    }");
        out.println("");
        out.println("    public override void Introduce()");
        out.println("    {");
        out.println("        Console.WriteLine($\"Hi, I'm {Name} from {School}\");");
        out.println("    }");
        out.println("}");
        out.println("                        </pre>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </section>");
    }
    
    private void printCollectionsSection(PrintWriter out) {
        out.println("        <section id=\"collections\" class=\"mb-5\">");
        out.println("            <div class=\"card\">");
        out.println("                <div class=\"card-header\">");
        out.println("                    <h3><i class=\"fas fa-layer-group\"></i> Collections</h3>");
        out.println("                </div>");
        out.println("                <div class=\"card-body\">");
        out.println("                    <div class=\"code-block\">");
        out.println("                        <pre>");
        out.println("// Arrays");
        out.println("int[] numbers = { 1, 2, 3, 4, 5 };");
        out.println("string[] fruits = new string[3] { \"Apple\", \"Banana\", \"Orange\" };");
        out.println("");
        out.println("// List<T>");
        out.println("List<string> cities = new List<string> { \"NYC\", \"London\" };");
        out.println("cities.Add(\"Paris\");");
        out.println("cities.Remove(\"London\");");
        out.println("");
        out.println("// Dictionary<TKey, TValue>");
        out.println("Dictionary<string, int> ages = new Dictionary<string, int>");
        out.println("{");
        out.println("    { \"Alice\", 30 },");
        out.println("    { \"Bob\", 25 }");
        out.println("};");
        out.println("ages[\"Charlie\"] = 35;");
        out.println("");
        out.println("// HashSet<T> - unique items");
        out.println("HashSet<string> uniqueNames = new HashSet<string> { \"John\", \"Jane\" };");
        out.println("");
        out.println("// Queue<T> and Stack<T>");
        out.println("Queue<string> queue = new Queue<string>();");
        out.println("queue.Enqueue(\"First\");");
        out.println("string first = queue.Dequeue();");
        out.println("");
        out.println("Stack<int> stack = new Stack<int>();");
        out.println("stack.Push(1);");
        out.println("int top = stack.Pop();");
        out.println("                        </pre>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </section>");
    }
}