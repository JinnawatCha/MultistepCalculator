import java.util.Stack
import java.util.Scanner
val scanner = Scanner(System.`in`)

fun main(){
    println("Welcome to Multi-step Calculator!!!")
    println("Enter your equation here: ")
    val equation = scanner.nextLine()
    if (equation != null) {
        try {
            val result = evaluateEquation(equation)
            println("Result of $equation: $result")
        } catch (e: Exception) {
            println("Error evaluating expression: ${e.message}")
        }
    } else {
        println("No equation entered.")
    }
}

//Separate number and operator in to each Stack
fun evaluateEquation(equation: String) : Int? {
    val tokens = equation.toCharArray()
    val value = Stack<Double>()
    val operators = Stack<Char>()

    var i = 0
    while (i < tokens.size) {
        if(tokens[i] in '0'..'9') {
            val sb = StringBuilder()
            while (i < tokens.size && tokens[i] in '0'..'9') {
                sb.append(tokens[i++])
            }
            value.push(sb.toString().toDouble())
            i--
        }
        else if(tokens[i] == '(') {
            operators.push(tokens[i])
        }
        else if (tokens[i] == ')') {
            while (operators.peek() != '('){
                value.push(applyOp(operators.pop(), value.pop(), value.pop()))
            }
            operators.pop()
        } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
            while (!operators.isEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                value.push(applyOp(operators.pop(), value.pop(), value.pop()))
            }
            operators.push(tokens[i])
        }
        i++
    }

    while (!operators.isEmpty()) {
        value.push(applyOp(operators.pop(), value.pop(), value.pop()))
    }
    return value.pop()?.toInt()
}

//Ordering Operations
fun hasPrecedence(op1: Char, op2: Char): Boolean {
    if (op2 == '(' || op2 == ')') {
        return false
    }
    if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
        return false
    }
    return true
}

//Apply Math Operations
fun applyOp(op: Char, b: Double, a: Double): Double {
    return when (op) {
        '+' -> a + b
        '-' -> a - b
        '*' -> a * b
        '/' -> {
            if (b == 0.0) throw UnsupportedOperationException("Cannot divide by zero")
            a / b
        }
        else -> 0.0
    }
}