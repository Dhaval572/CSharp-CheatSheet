<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C# Code Runner | Live Interpreter</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <style>
        .code-editor { font-family: 'Consolas', monospace; background: #1e1e1e; color: #d4d4d4; border: none; resize: vertical; }
        .output-area { background: #0d1117; color: #f0f6fc; font-family: 'Consolas', monospace; border: none; min-height: 200px; }
        .run-btn { background: linear-gradient(45deg, #28a745, #20c997); border: none; }
        .copy-code-btn { position: absolute; top: 10px; right: 10px; }
    </style>
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
            <h1><i class="fas fa-play-circle"></i> C# Code Runner</h1>
            <p>Write, run, and test C# code instantly with our live interpreter</p>
        </div>
    </header>
    
    <!-- Navigation -->
    <nav class="navigation">
        <div class="container">
            <div class="nav-menu">
                <a href="index.html" class="nav-item">Home</a>
                <a href="CheatsheetServlet" class="nav-item">C# Cheatsheet</a>
                <a href="CodeRunnerServlet" class="nav-item active">Code Runner</a>
                <a href="#examples" class="nav-item">Examples</a>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <main class="main-content">
        <div class="container">
            <!-- Code Editor Section -->
            <section class="mb-5">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h3><i class="fas fa-code"></i> C# Code Editor</h3>
                        <div>
                            <button id="runBtn" class="btn run-btn text-white me-2">
                                <i class="fas fa-play"></i> Run Code
                            </button>
                            <button id="clearBtn" class="btn btn-secondary">
                                <i class="fas fa-trash"></i> Clear
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="position-relative">
                            <textarea id="codeEditor" class="form-control code-editor" rows="15" placeholder="Write your C# code here...">// Welcome to C# Code Runner!
// Try running this example code:

int x = 10;
int y = 25;
int sum = x + y;

Console.WriteLine("Hello, C# World!");
Console.WriteLine($"The sum of {x} and {y} is {sum}");

string name = "Developer";
Console.WriteLine($"Welcome, {name}!");</textarea>
                            <button class="btn btn-sm btn-outline-light copy-code-btn" onclick="copyCode()">
                                <i class="fas fa-copy"></i> Copy
                            </button>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- Output Section -->
            <section class="mb-5">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h3><i class="fas fa-terminal"></i> Output</h3>
                        <button id="copyOutputBtn" class="btn btn-sm btn-outline-primary" onclick="copyOutput()">
                            <i class="fas fa-copy"></i> Copy Output
                        </button>
                    </div>
                    <div class="card-body">
                        <textarea id="outputArea" class="form-control output-area" rows="8" readonly placeholder="Code output will appear here..."></textarea>
                    </div>
                </div>
            </section>
            
            <!-- Examples Section -->
            <section id="examples" class="mb-5">
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-lightbulb"></i> Example Code Snippets</h3>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <!-- Example 1: Variables -->
                            <div class="col-md-6 mb-3">
                                <div class="card h-100">
                                    <div class="card-body">
                                        <h5>Variables & Math</h5>
                                        <div class="code-block">
                                            <pre>int a = 15;
int b = 4;
Console.WriteLine($"Sum: {a + b}");
Console.WriteLine($"Product: {a * b}");
Console.WriteLine($"Division: {a / b}");</pre>
                                        </div>
                                        <button class="btn btn-sm btn-primary" onclick="loadExample1()">Load Example</button>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Example 2: Strings -->
                            <div class="col-md-6 mb-3">
                                <div class="card h-100">
                                    <div class="card-body">
                                        <h5>String Operations</h5>
                                        <div class="code-block">
                                            <pre>string firstName = "John";
string lastName = "Doe";
string fullName = firstName + " " + lastName;
Console.WriteLine($"Hello, {fullName}!");
Console.WriteLine($"Length: {fullName.Length}");</pre>
                                        </div>
                                        <button class="btn btn-sm btn-primary" onclick="loadExample2()">Load Example</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </main>
    
    <!-- Footer -->
    <footer class="footer">
        <div class="container text-center">
            <p>&copy; 2024 C# Code Runner. Powered by C++ Interpreter</p>
        </div>
    </footer>
    
    <!-- JavaScript -->
    <script>
        const runBtn = document.getElementById('runBtn');
        const clearBtn = document.getElementById('clearBtn');
        const codeEditor = document.getElementById('codeEditor');
        const outputArea = document.getElementById('outputArea');

        runBtn.addEventListener('click', runCode);
        clearBtn.addEventListener('click', clearCode);

        function runCode() {
            const code = codeEditor.value.trim();
            if (!code) {
                outputArea.value = 'Please enter some C# code to run.';
                return;
            }

            runBtn.disabled = true;
            runBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Running...';
            outputArea.value = 'Executing code...';

            fetch('CodeRunnerServlet', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'action=run_code&csharp_code=' + encodeURIComponent(code)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    outputArea.value = data.output || 'Code executed successfully (no output)';
                } else {
                    outputArea.value = 'Error: ' + (data.error || 'Unknown error occurred');
                }
            })
            .catch(error => {
                outputArea.value = 'Network error: ' + error.message;
            })
            .finally(() => {
                runBtn.disabled = false;
                runBtn.innerHTML = '<i class="fas fa-play"></i> Run Code';
            });
        }

        function clearCode() {
            codeEditor.value = '';
            outputArea.value = '';
        }

        function copyCode() {
            codeEditor.select();
            document.execCommand('copy');
            showToast('Code copied to clipboard!');
        }

        function copyOutput() {
            outputArea.select();
            document.execCommand('copy');
            showToast('Output copied to clipboard!');
        }

        function loadExample1() {
            const code = 'int a = 15;\nint b = 4;\nConsole.WriteLine($"Sum: {a + b}");\nConsole.WriteLine($"Product: {a * b}");\nConsole.WriteLine($"Division: {a / b}");';
            codeEditor.value = code;
            showToast('Example loaded!');
        }

        function loadExample2() {
            const code = 'string firstName = "John";\nstring lastName = "Doe";\nstring fullName = firstName + " " + lastName;\nConsole.WriteLine($"Hello, {fullName}!");\nConsole.WriteLine($"Length: {fullName.Length}");';
            codeEditor.value = code;
            showToast('Example loaded!');
        }

        function loadExample(code) {
            codeEditor.value = code;
            showToast('Example loaded!');
        }

        function showToast(message) {
            // Simple toast notification
            const toast = document.createElement('div');
            toast.style.cssText = 'position:fixed;top:20px;right:20px;background:#28a745;color:white;padding:12px 20px;border-radius:5px;z-index:9999;';
            toast.textContent = message;
            document.body.appendChild(toast);
            setTimeout(() => toast.remove(), 3000);
        }
    </script>
</body>
</html>