#include <iostream>
#include <string>
#include <vector>
#include <array>
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
                    string eval_expr = SubstituteVariables(expression);
                    replacement = to_string(static_cast<int>(EvaluateArithmetic(eval_expr)));
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
        return expr.find(" + ") != string::npos && (expr.find('"') != string::npos || b_HasStringVariable(expr));
    }

    // Check if expression has string variables
    bool b_HasStringVariable(const string &expr)
    {
        for (const auto &pair : variables)
        {
            if (expr.find(pair.first) != string::npos)
            {
                // Check if it's a string value (non-numeric)
                bool b_IsNumeric = true;
                for (char c : pair.second)
                {
                    if (!isdigit(c) && c != '.' && c != '-')
                    {
                        b_IsNumeric = false;
                        break;
                    }
                }
                if (!b_IsNumeric)
                {
                    return true;
                }
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
            size_t next_plus = result.find(" + ", pos);
            if (next_plus == string::npos)
            {
                parts.push_back(Trim(result.substr(pos)));
                break;
            }
            parts.push_back(Trim(result.substr(pos, next_plus - pos)));
            pos = next_plus + 3;
        }

        string concatenated = "";
        for (const string &part : parts)
        {
            string value = Trim(part);

            // Remove quotes if present
            if 
            (
                value.length() >= 2 
                && value.front() == '"' 
                && value.back() == '"'
            )
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
                string sub_expr = result.substr(start + 1, end - start - 1);
                string sub_result = SubstituteVariables(sub_expr);

                if (ContainsArithmetic(sub_result))
                {
                    sub_result = to_string(static_cast<int>(EvaluateArithmetic(sub_result)));
                }

                result.replace(start, end - start + 1, sub_result);
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
            string clean_expr = expr;

            // Remove extra spaces
            clean_expr.erase(remove(clean_expr.begin(), clean_expr.end(), ' '), clean_expr.end());

            // Simple expression parser with precedence
            return ParseExpression(clean_expr);
        }
        catch (...)
        {
            return 0;
        }
    }

    // Parse expression with operator precedence
    double ParseExpression(const string &expr)
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
    void ProcessConsoleWriteLine(const string &line)
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
    void ProcessVariableDeclaration(const string &line)
    {
        // Look for type var_name = value;
        array<string, 8> types = {"int", "string", "double", "float", "bool", "long", "decimal", "char"};

        for (const string &type : types)
        {
            size_t type_pos = line.find(type + " ");
            if (type_pos != string::npos)
            {
                size_t name_start = type_pos + type.length() + 1;
                size_t equal_pos = line.find('=', name_start);
                size_t semicolon_pos = line.find(';', equal_pos);

                if (equal_pos != string::npos && semicolon_pos != string::npos)
                {
                    string var_name = 
                    Trim
                    (
                        line.substr
                        (
                            name_start, 
                            equal_pos - name_start
                        )
                    );
                    string value = Trim(line.substr(equal_pos + 1, semicolon_pos - equal_pos - 1));

                    string evaluated_value = EvaluateExpression(value);
                    variables[var_name] = evaluated_value;
                    return;
                }
            }
        }
    }

    // Process variable assignments
    void processAssignment(const string &line)
    {
        size_t equal_pos = line.find('=');
        size_t semicolon_pos = line.find(';');

        if (equal_pos != string::npos && semicolon_pos != string::npos)
        {
            string var_name = Trim(line.substr(0, equal_pos));
            string value = Trim(line.substr(equal_pos + 1, semicolon_pos - equal_pos - 1));

            if (variables.find(var_name) != variables.end())
            {
                string evaluated_value = EvaluateExpression(value);
                variables[var_name] = evaluated_value;
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
                ProcessConsoleWriteLine(line);
            }
            else if (isVariableDeclaration(line))
            {
                ProcessVariableDeclaration(line);
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
                ProcessConsoleWriteLine(line);
            }
            else if (isVariableDeclaration(line))
            {
                ProcessVariableDeclaration(line);
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