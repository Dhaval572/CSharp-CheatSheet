<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C# Complete Cheatsheet</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
            <h1><i class="fab fa-microsoft"></i> C# Complete Cheatsheet</h1>
            <p>Your comprehensive guide to C# programming language</p>
        </div>
    </header>
    
    <!-- Navigation -->
    <nav class="navigation">
        <div class="container">
            <div class="nav-menu">
                <a href="#variables" class="nav-item">Variables</a>
                <a href="#operators" class="nav-item">Operators</a>
                <a href="#control-flow" class="nav-item">Control Flow</a>
                <a href="#methods" class="nav-item">Methods</a>
                <a href="#oop" class="nav-item">OOP</a>
                <a href="#collections" class="nav-item">Collections</a>
                <a href="CodeRunnerServlet" class="nav-item">Try Code</a>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <main class="main-content">
        <div class="container">
            <!-- Variables Section -->
            <section id="variables" class="mb-5">
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-database"></i> Variables & Data Types</h3>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h5>Value Types</h5>
                                <div class="code-block">
                                    <pre>int age = 25;
long population = 7800000000L;
float price = 19.99f;
double distance = 123.456;
decimal money = 99.99m;
bool isActive = true;
char grade = 'A';</pre>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <h5>Reference Types</h5>
                                <div class="code-block">
                                    <pre>string name = "John Doe";
string multiline = @"Line 1
Line 2";
string interpolated = $"Hello {name}";

int? nullableAge = null;
var autoInt = 42;
const double PI = 3.14159;</pre>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Operators Section -->
            <section id="operators" class="mb-5">
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-calculator"></i> Operators</h3>
                    </div>
                    <div class="card-body">
                        <div class="code-block">
                            <pre>// Arithmetic: +, -, *, /, %
int sum = 10 + 5;   // 15
int diff = 10 - 5;  // 5
int product = 10 * 5; // 50

// Comparison: ==, !=, >, <, >=, <=
bool equal = (a == b);
bool greater = (a > b);

// Logical: &&, ||, !
bool result = (a > 5) && (b < 10);

// Assignment: =, +=, -=, *=, /=
x += 5;  // x = x + 5

// Null-coalescing: ??, ??=
string result = name ?? "default";</pre>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Control Flow Section -->
            <section id="control-flow" class="mb-5">
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-code-branch"></i> Control Flow & Loops</h3>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="code-block">
                                    <pre>// If-else
if (score >= 90)
    grade = "A";
else if (score >= 80)
    grade = "B";
else
    grade = "F";

// Ternary
string status = (score >= 60) ? "Pass" : "Fail";

// Switch
switch (day)
{
    case 1: dayName = "Monday"; break;
    case 2: dayName = "Tuesday"; break;
    default: dayName = "Unknown"; break;
}</pre>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="code-block">
                                    <pre>// For loop
for (int i = 0; i < 5; i++)
{
    Console.WriteLine(i);
}

// While loop
while (count < 3)
{
    count++;
}

// Foreach loop
foreach (string item in items)
{
    Console.WriteLine(item);
}</pre>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Methods Section -->
            <section id="methods" class="mb-5">
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-function"></i> Methods</h3>
                    </div>
                    <div class="card-body">
                        <div class="code-block">
                            <pre>// Basic method
public static void SayHello()
{
    Console.WriteLine("Hello!");
}

// Method with parameters and return
public static int Add(int a, int b)
{
    return a + b;
}

// Method overloading
public static double Add(double a, double b) => a + b;

// Default parameters
public static void Greet(string name = "World")
{
    Console.WriteLine($"Hello {name}!");
}</pre>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- OOP Section -->
            <section id="oop" class="mb-5">
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-cube"></i> Object-Oriented Programming</h3>
                    </div>
                    <div class="card-body">
                        <div class="code-block">
                            <pre>// Class definition
public class Person
{
    public string Name { get; set; }
    public int Age { get; set; }

    public Person(string name, int age)
    {
        Name = name;
        Age = age;
    }

    public virtual void Introduce()
    {
        Console.WriteLine($"Hi, I'm {Name}");
    }
}

// Inheritance
public class Student : Person
{
    public string School { get; set; }

    public Student(string name, int age, string school) 
        : base(name, age)
    {
        School = school;
    }

    public override void Introduce()
    {
        Console.WriteLine($"Hi, I'm {Name} from {School}");
    }
}</pre>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Collections Section -->
            <section id="collections" class="mb-5">
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-layer-group"></i> Collections</h3>
                    </div>
                    <div class="card-body">
                        <div class="code-block">
                            <pre>// Arrays
int[] numbers = { 1, 2, 3, 4, 5 };
string[] fruits = new string[3] { "Apple", "Banana", "Orange" };

// List&lt;T&gt;
List&lt;string&gt; cities = new List&lt;string&gt; { "NYC", "London" };
cities.Add("Paris");
cities.Remove("London");

// Dictionary&lt;TKey, TValue&gt;
Dictionary&lt;string, int&gt; ages = new Dictionary&lt;string, int&gt;
{
    { "Alice", 30 },
    { "Bob", 25 }
};
ages["Charlie"] = 35;

// HashSet&lt;T&gt; - unique items
HashSet&lt;string&gt; uniqueNames = new HashSet&lt;string&gt; { "John", "Jane" };

// Queue&lt;T&gt; and Stack&lt;T&gt;
Queue&lt;string&gt; queue = new Queue&lt;string&gt;();
queue.Enqueue("First");
string first = queue.Dequeue();

Stack&lt;int&gt; stack = new Stack&lt;int&gt;();
stack.Push(1);
int top = stack.Pop();</pre>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </main>
    
    <!-- Footer -->
    <footer class="footer">
        <div class="container text-center">
            <p>&copy; 2024 C# Cheatsheet. Database: <strong>Csharpdb</strong></p>
        </div>
    </footer>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function copyCodeBlock(button) {
            const codeBlock = button.parentElement.querySelector('pre');
            const text = codeBlock.textContent;
            navigator.clipboard.writeText(text).then(() => {
                const originalText = button.innerHTML;
                button.innerHTML = '<i class="fas fa-check"></i> Copied!';
                button.classList.remove('btn-outline-primary');
                button.classList.add('btn-success');
                setTimeout(() => {
                    button.innerHTML = originalText;
                    button.classList.remove('btn-success');
                    button.classList.add('btn-outline-primary');
                }, 2000);
            }).catch(err => {
                console.error('Failed to copy: ', err);
                button.innerHTML = '<i class="fas fa-times"></i> Failed!';
            });
        }
    </script>
</body>
</html>