# C# Developer Cheatsheet

A comprehensive, interactive C# reference guide with Bootstrap UI, copy-paste functionality, and a simple C++ based code interpreter.

## Features

🔥 **Interactive Cheatsheet**
- Beautiful Bootstrap-based UI with modern design
- Copy-paste functionality for all code examples
- Syntax highlighting with Prism.js
- Search functionality to find specific topics
- Responsive design for desktop and mobile

🚀 **Comprehensive C# Coverage**
- Variables and Data Types
- Operators and Expressions
- Control Flow (If-Else, Switch, Loops)
- Methods and Functions
- Object-Oriented Programming
- Collections and LINQ
- Exception Handling
- Async/Await Patterns

📚 **Documentation & Resources**
- .NET Framework documentation
- Essential namespaces reference
- Official Microsoft documentation links
- Learning platforms and tutorials
- Development tools recommendations
- Community resources and forums

🛠️ **Simple C# Code Interpreter**
- Written in C++ for educational purposes
- Supports basic C# syntax execution
- Variable declarations and assignments
- Console.WriteLine statements
- String interpolation
- Simple arithmetic operations

## Getting Started

### Web Interface

1. **Using Tomcat Server:**
   ```bash
   mvn clean compile
   mvn tomcat7:run
   ```
   Then open `http://localhost:8080/C#_cheatsheet/`

2. **Direct HTML Access:**
   Open `src/main/webapp/index.html` in your web browser

### C++ Interpreter

#### Windows (using provided batch file):
```cmd
build_interpreter.bat
```

#### Manual Compilation:

**With Visual Studio:**
```cmd
cl /std:c++20 /EHsc /O2 src\main\cpp\CSharpInterpreter.cpp /FeCSharpInterpreter.exe
```

**With g++:**
```cmd
g++ -std=c++20 -O2 -o CSharpInterpreter.exe src\main\cpp\CSharpInterpreter.cpp
```

**With CMake:**
```cmd
mkdir build
cd build
cmake ..
cmake --build .
```

## Usage

### Web Cheatsheet
- Browse different C# topics using the sidebar navigation
- Click **Copy** buttons to copy code to clipboard
- Click **Run** buttons to simulate code execution
- Use the search box to find specific concepts
- All external links open in new tabs

### C++ Interpreter
The interpreter supports basic C# constructs:

```csharp
// Variable declarations
int x = 5;
string name = "Alice";

// Arithmetic operations
int sum = x + 3;

// Console output
Console.WriteLine("Hello, World!");
Console.WriteLine(sum);
Console.WriteLine($"Hello, {name}!");
```

**Supported Features:**
- `int` and `string` variable declarations
- Basic arithmetic (`+`, `-`, `*`, `/`)
- `Console.WriteLine()` with literals and variables
- String interpolation with `$"{}"`
- Simple variable assignments

**Limitations:**
- This is a simplified educational interpreter
- Does not support full C# language features
- No class definitions, methods, or complex expressions
- For complete C# development, use Visual Studio or .NET SDK

## Project Structure

```
C# cheetsheet/
├── src/
│   ├── main/
│   │   ├── cpp/
│   │   │   └── CSharpInterpreter.cpp    # Simple C++ interpreter
│   │   ├── java/
│   │   │   └── MyServlet.java           # Java servlet (legacy)
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml              # Web configuration
│   │       └── index.html               # Main cheatsheet page
│   └── target/                          # Build output
├── CMakeLists.txt                       # CMake build file
├── build_interpreter.bat               # Windows build script
├── pom.xml                             # Maven configuration
└── README.md                           # This file
```

## Technologies Used

### Frontend
- **HTML5** - Structure and semantics
- **CSS3** - Modern styling with CSS Grid and Flexbox
- **Bootstrap 5.3** - Responsive UI framework
- **JavaScript ES6+** - Interactive functionality
- **Prism.js** - Syntax highlighting
- **Font Awesome** - Icons

### Backend/Tools
- **Java Servlets** - Web server support
- **Maven** - Build and dependency management
- **C++20** - Simple interpreter implementation
- **CMake** - Cross-platform build system

## Browser Support

- Chrome 90+ ✅
- Firefox 88+ ✅
- Safari 14+ ✅
- Edge 90+ ✅

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add your C# examples or improvements
4. Test the changes
5. Submit a pull request

## Useful Resources

### Official Documentation
- [Microsoft C# Documentation](https://docs.microsoft.com/en-us/dotnet/csharp/)
- [.NET API Reference](https://docs.microsoft.com/en-us/dotnet/api/)
- [C# Programming Guide](https://docs.microsoft.com/en-us/dotnet/csharp/programming-guide/)

### Development Tools
- [Visual Studio IDE](https://visualstudio.microsoft.com/)
- [Visual Studio Code](https://code.visualstudio.com/)
- [.NET SDK](https://dotnet.microsoft.com/download)

### Learning Resources
- [Microsoft Learn](https://docs.microsoft.com/en-us/learn/browse/?products=dotnet)
- [C# Corner](https://www.c-sharpcorner.com/)
- [Stack Overflow C#](https://stackoverflow.com/questions/tagged/c%23)

## License

This project is created for educational purposes. Feel free to use and modify as needed.

## Support

If you find this cheatsheet helpful, consider:
- ⭐ Starring the repository
- 🐛 Reporting bugs or issues
- 💡 Suggesting improvements
- 📚 Adding more examples

---

**Happy Coding! 🚀**