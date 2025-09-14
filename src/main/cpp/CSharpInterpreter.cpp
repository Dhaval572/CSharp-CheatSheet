#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <sstream>
#include <algorithm>
#include <fstream>
#include <cctype>

using namespace std;

class CSharpInterpreter
{
private:
    map<string, string> variables;
    vector<string> output;

    // Helper function to trim whitespace
    string Trim(const string &str)
    {
        int start = str.find_first_not_of(" \t\n\r");
        if (start == string::npos)
        {
            return "";
        }

        int end = str.find_last_not_of(" \t\n\r");
        return str.substr(start, end - start + 1);
    }

    // Check if string contains substring
    bool Contains(const string &str, const string &sub)
    {
        return str.find(sub) != string::npos;
    }

    // Simple variable substitution
    string SubstituteVariables(string expr)
    {
        for (const auto &pair : variables)
        {
            size_t pos = 0;
            while ((pos = expr.find(pair.first, pos)) != string::npos)
            {
                // Simple word boundary check
                bool b_IsWordStart = (pos == 0 || !isalnum(expr[pos - 1]));
                bool b_IsWordEnd = (pos + pair.first.length() >= expr.length() ||
                                  !isalnum(expr[pos + pair.first.length()]));

                if (b_IsWordStart && b_IsWordEnd)
                {
                    expr.replace(pos, pair.first.length(), pair.second);
                    pos += pair.second.length();
                }
                else
                {
                    pos++;
                }
            }
        }
        return expr;
    }

    // Enhanced expression evaluator
    string EvaluateExpression(const string &expr)
    {
        string clean_expr = Trim(expr);

        // Handle string literals
        if 
        (   clean_expr.length() >= 2 
            && clean_expr.front() == '"' 
            && clean_expr.back() == '"'
        )
        {
            return clean_expr.substr(1, clean_expr.length() - 2);
        }

        // Handle string interpolation
        if 
        (   
            clean_expr.length() > 2 
            && clean_expr[0] == '$' 
            && clean_expr[1] == '"' 
            && clean_expr.back() == '"'
        )
        {
            string interpolated = clean_expr.substr(2, clean_expr.length() - 3);
            return ProcessStringInterpolation(interpolated);
        }

        // Handle string concatenation
        if (ContainsStringConcatenation(clean_expr))
        {
            return EvaluateStringConcatenation(clean_expr);
        }

        // Handle complex expressions in parentheses
        if (containsParentheses(clean_expr))
        {
            return EvaluateComplexExpression(clean_expr);
        }

        // Substitute variables
        clean_expr = SubstituteVariables(clean_expr);

        // Handle arithmetic
        if (ContainsArithmetic(clean_expr))
        {
            return to_string(static_cast<int>(EvaluateArithmetic(clean_expr)));
        }

        return clean_expr;
    }

    // Process string interpolation with expression evaluation
    string ProcessStringInterpolation(const string &str)
    {
        string result = str;

        // Find and replace {expression} patterns
        size_t pos = 0;
        while ((pos = result.find('{', pos)) != string::npos)
        {
            size_t end = result.find('}', pos);
            if (end != string::npos)
            {
                string expression = result.substr(pos + 1, end - pos - 1);
                string replacement;

                // Evaluate expressions within interpolation
                if (ContainsArithmetic(expression))
                {
                    string evalExpr = SubstituteVariables(expression);
                    replacement = to_string(static_cast<int>(EvaluateArithmetic(evalExpr)));
                }
                else
                {
                    replacement = (variables.find(expression) != variables.end()) ? variables[expression] : expression;
                }

                result.replace(pos, end - pos + 1, replacement);
                pos += replacement.length();
            }
            else
            {
                break;
            }
        }

        return result;
    }

    // Check for string concatenation
    bool ContainsStringConcatenation(const string &expr)
    {
        return expr.find(" + ") != string::npos && (expr.find('"') != string::npos || hasStringVariable(expr));
    }

    // Check if expression has string variables
    bool hasStringVariable(const string &expr)
    {
        for (const auto &pair : variables)
        {
            if (expr.find(pair.first) != string::npos)
            {
                // Check if it's a string value (non-numeric)
                bool isNumeric = true;
                for (char c : pair.second)
                {
                    if (!isdigit(c) && c != '.' && c != '-')
                    {
                        isNumeric = false;
                        break;
                    }
                }
                if (!isNumeric)
                    return true;
            }
        }
        return false;
    }

    // Evaluate string concatenation
    string EvaluateStringConcatenation(const string &expr)
    {
        string result = expr;

        // Split by ' + ' and concatenate parts
        vector<string> parts;
        size_t pos = 0;
        while (pos < result.length())
        {
            size_t nextPlus = result.find(" + ", pos);
            if (nextPlus == string::npos)
            {
                parts.push_back(Trim(result.substr(pos)));
                break;
            }
            parts.push_back(Trim(result.substr(pos, nextPlus - pos)));
            pos = nextPlus + 3;
        }

        string concatenated = "";
        for (const string &part : parts)
        {
            string value = Trim(part);

            // Remove quotes if present
            if (value.length() >= 2 && value.front() == '"' && value.back() == '"')
            {
                concatenated += value.substr(1, value.length() - 2);
            }
            else if (variables.find(value) != variables.end())
            {
                concatenated += variables[value];
            }
            else
            {
                concatenated += value;
            }
        }

        return concatenated;
    }

    // Check for parentheses
    bool containsParentheses(const string &expr)
    {
        return expr.find('(') != string::npos && expr.find(')') != string::npos;
    }

    // Evaluate complex expressions with parentheses
    string EvaluateComplexExpression(const string &expr)
    {
        string result = expr;

        // Find and evaluate parentheses first
        while (result.find('(') != string::npos)
        {
            size_t start = result.find_last_of('(');
            size_t end = result.find(')', start);

            if (end != string::npos)
            {
                string subExpr = result.substr(start + 1, end - start - 1);
                string subResult = SubstituteVariables(subExpr);

                if (ContainsArithmetic(subResult))
                {
                    subResult = to_string(static_cast<int>(EvaluateArithmetic(subResult)));
                }

                result.replace(start, end - start + 1, subResult);
            }
            else
            {
                break;
            }
        }

        // Now evaluate the remaining expression
        result = SubstituteVariables(result);
        if (ContainsArithmetic(result))
        {
            return to_string(static_cast<int>(EvaluateArithmetic(result)));
        }

        return result;
    }

    // Check for arithmetic operators (but not in string context)
    bool ContainsArithmetic(const string &expr)
    {
        // Don't treat + as arithmetic if it's string concatenation
        if (expr.find('"') != string::npos && expr.find(" + ") != string::npos)
        {
            return false;
        }
        return expr.find_first_of("+-*/") != string::npos;
    }

    // Enhanced arithmetic evaluator with proper operator precedence
    double EvaluateArithmetic(const string &expr)
    {
        try
        {
            // Handle multiplication and division first (operator precedence)
            string cleanExpr = expr;

            // Remove extra spaces
            cleanExpr.erase(remove(cleanExpr.begin(), cleanExpr.end(), ' '), cleanExpr.end());

            // Simple expression parser with precedence
            return parseExpression(cleanExpr);
        }
        catch (...)
        {
            return 0;
        }
    }

    // Parse expression with operator precedence
    double parseExpression(const string &expr)
    {
        vector<double> numbers;
        vector<char> operators;

        // Parse numbers and operators
        string current = "";
        for (size_t i = 0; i < expr.length(); i++)
        {
            char c = expr[i];
            if (isdigit(c) || c == '.')
            {
                current += c;
            }
            else if (c == '+' || c == '-' || c == '*' || c == '/')
            {
                if (!current.empty())
                {
                    numbers.push_back(stod(current));
                    current = "";
                }
                operators.push_back(c);
            }
        }
        if (!current.empty())
        {
            numbers.push_back(stod(current));
        }

        // Apply multiplication and division first
        for (size_t i = 0; i < operators.size();)
        {
            if (operators[i] == '*' || operators[i] == '/')
            {
                double result;
                if (operators[i] == '*')
                {
                    result = numbers[i] * numbers[i + 1];
                }
                else
                {
                    result = numbers[i + 1] != 0 ? numbers[i] / numbers[i + 1] : 0;
                }
                numbers[i] = result;
                numbers.erase(numbers.begin() + i + 1);
                operators.erase(operators.begin() + i);
            }
            else
            {
                i++;
            }
        }

        // Apply addition and subtraction
        double result = numbers[0];
        for (size_t i = 0; i < operators.size(); i++)
        {
            if (operators[i] == '+')
            {
                result += numbers[i + 1];
            }
            else if (operators[i] == '-')
            {
                result -= numbers[i + 1];
            }
        }

        return result;
    }

    // Process Console.WriteLine statements
    void processConsoleWriteLine(const string &line)
    {
        // Find Console.WriteLine(...)
        size_t start = line.find("Console.WriteLine(");
        if (start == string::npos)
            return;

        start += 18; // Length of "Console.WriteLine("
        size_t end = line.find_last_of(')');
        if (end == string::npos || end <= start)
            return;

        string content = Trim(line.substr(start, end - start));
        string result = EvaluateExpression(content);
        output.push_back(result);
    }

    // Enhanced variable declaration processing
    void processVariableDeclaration(const string &line)
    {
        // Look for type varName = value;
        vector<string> types = {"int", "string", "double", "float", "bool", "long", "decimal", "char"};

        for (const string &type : types)
        {
            size_t typePos = line.find(type + " ");
            if (typePos != string::npos)
            {
                size_t nameStart = typePos + type.length() + 1;
                size_t equalPos = line.find('=', nameStart);
                size_t semicolonPos = line.find(';', equalPos);

                if (equalPos != string::npos && semicolonPos != string::npos)
                {
                    string varName = Trim(line.substr(nameStart, equalPos - nameStart));
                    string value = Trim(line.substr(equalPos + 1, semicolonPos - equalPos - 1));

                    string evaluatedValue = EvaluateExpression(value);
                    variables[varName] = evaluatedValue;
                    return;
                }
            }
        }
    }

    // Process variable assignments
    void processAssignment(const string &line)
    {
        size_t equalPos = line.find('=');
        size_t semicolonPos = line.find(';');

        if (equalPos != string::npos && semicolonPos != string::npos)
        {
            string varName = Trim(line.substr(0, equalPos));
            string value = Trim(line.substr(equalPos + 1, semicolonPos - equalPos - 1));

            if (variables.find(varName) != variables.end())
            {
                string evaluatedValue = EvaluateExpression(value);
                variables[varName] = evaluatedValue;
            }
        }
    }

public:
    // Main interpretation method with file I/O support
    vector<string> InterpretFromFile(const string &filename)
    {
        output.clear();
        variables.clear();

        ifstream file(filename);
        if (!file.is_open())
        {
            output.push_back("Error: Could not open file " + filename);
            return output;
        }

        string line;
        while (getline(file, line))
        {
            line = Trim(line);

            // Skip empty lines and comments
            if (line.empty() || line.substr(0, 2) == "//")
            {
                continue;
            }

            // Process different types of statements
            if (Contains(line, "Console.WriteLine"))
            {
                processConsoleWriteLine(line);
            }
            else if (isVariableDeclaration(line))
            {
                processVariableDeclaration(line);
            }
            else if (isAssignment(line))
            {
                processAssignment(line);
            }
        }

        file.close();
        return output;
    }

    // Direct string interpretation
    vector<string> interpret(const string &code)
    {
        output.clear();
        variables.clear();

        istringstream stream(code);
        string line;

        while (getline(stream, line))
        {
            line = Trim(line);

            // Skip empty lines and comments
            if (line.empty() || line.substr(0, 2) == "//")
            {
                continue;
            }

            // Process different types of statements
            if (Contains(line, "Console.WriteLine"))
            {
                processConsoleWriteLine(line);
            }
            else if (isVariableDeclaration(line))
            {
                processVariableDeclaration(line);
            }
            else if (isAssignment(line))
            {
                processAssignment(line);
            }
        }

        return output;
    }

    // Helper methods
    bool isVariableDeclaration(const string &line)
    {
        vector<string> types = {"int ", "string ", "double ", "float ", "bool ", "long ", "decimal ", "char "};
        for (const string &type : types)
        {
            if (Contains(line, type) && Contains(line, "="))
            {
                return true;
            }
        }
        return false;
    }

    bool isAssignment(const string &line)
    {
        return Contains(line, "=") && !line.empty() && line.back() == ';' && !isVariableDeclaration(line);
    }

    // Get current variables
    map<string, string> getVariables() const
    {
        return variables;
    }

    // Write output to file
    void WriteOutputToFile(const string &filename) const
    {
        ofstream file(filename);
        if (file.is_open())
        {
            for (const auto &line : output)
            {
                file << line << "\n";
            }
            file.close();
        }
    }
};

// Enhanced main function with command-line support
int main(int argc, char *argv[])
{
    CSharpInterpreter interpreter;

    if (argc > 1)
    {
        // File mode: read from command line argument
        string input_file = argv[1];
        string output_file = argc > 2 ? argv[2] : "output.txt";

        auto results = interpreter.InterpretFromFile(input_file);

        // Write results to output file
        interpreter.WriteOutputToFile(output_file);

        // Also print to console
        for (const auto &result : results)
        {
            cout << result << endl;
        }
    }
    else
    {
        // Interactive mode: Print ready message for web integration
        cout << "C# Interpreter Ready - Command line usage: CSharpInterpreter.exe <inputfile> [outputfile]" << endl;
    }

    return 0;
}