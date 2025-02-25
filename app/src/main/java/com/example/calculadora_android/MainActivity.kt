package com.example.calculadora_android
// Ajuste o pacote conforme seu projeto

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: EditText
    private var currentInput = "" // Armazena o número atual sendo digitado
    private var result = 0.0 // Armazena o resultado acumulado
    private var operator = "" // Operador atual
    private var pendingOperation = "" // Operação pendente a ser exibida

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincula o EditText
        displayTextView = findViewById(R.id.displayTextView)

        // Vincula os botões
        val btnClear: Button = findViewById(R.id.btnClear)
        val btnBackspace: Button = findViewById(R.id.btnBackspace)
        val btnPercent: Button = findViewById(R.id.btnPercent)
        val btnDivide: Button = findViewById(R.id.btnDivide)
        val btnMultiply: Button = findViewById(R.id.btnMultiply)
        val btnSubtract: Button = findViewById(R.id.btnSubtract)
        val btnAdd: Button = findViewById(R.id.btnAdd)
        val btnEquals: Button = findViewById(R.id.btnEquals)
        val btnDot: Button = findViewById(R.id.btnDot)
        val btn0: Button = findViewById(R.id.btn0)
        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btn4: Button = findViewById(R.id.btn4)
        val btn5: Button = findViewById(R.id.btn5)
        val btn6: Button = findViewById(R.id.btn6)
        val btn7: Button = findViewById(R.id.btn7)
        val btn8: Button = findViewById(R.id.btn8)
        val btn9: Button = findViewById(R.id.btn9)

        // Listeners para botões numéricos
        val numberButtons = listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
        numberButtons.forEach { button ->
            button.setOnClickListener {
                appendNumber(button.text.toString())
            }
        }

        // Listener para o ponto decimal
        btnDot.setOnClickListener {
            appendDot()
        }

        // Listeners para operadores
        btnAdd.setOnClickListener { setOperator("+") }
        btnSubtract.setOnClickListener { setOperator("-") }
        btnMultiply.setOnClickListener { setOperator("×") }
        btnDivide.setOnClickListener { setOperator("÷") }
        btnPercent.setOnClickListener { calculatePercent() }

        // Listener para igual
        btnEquals.setOnClickListener { calculateResult() }

        // Listener para limpar
        btnClear.setOnClickListener { clear() }

        // Listener para backspace
        btnBackspace.setOnClickListener { backspace() }
    }

    // Adiciona um número ao display
    private fun appendNumber(number: String) {
        if (currentInput == "0" && operator.isEmpty()) {
            currentInput = number
        } else if (pendingOperation.isNotEmpty()) {
            currentInput = number
            pendingOperation = ""
        } else {
            currentInput += number
        }
        updateDisplay(if (operator.isEmpty()) currentInput else "${formatResult(result)} $operator $currentInput")
    }

    // Adiciona um ponto decimal
    private fun appendDot() {
        if (!currentInput.contains(".")) {
            if (currentInput.isEmpty() || pendingOperation.isNotEmpty()) {
                currentInput = "0"
                pendingOperation = ""
            }
            currentInput += "."
            updateDisplay(if (operator.isEmpty()) currentInput else "${formatResult(result)} $operator $currentInput")
        }
    }

    // Define o operador e calcula o resultado parcial se já houver uma operação
    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            try {
                if (operator.isNotEmpty()) {
                    calculateResult() // Calcula o resultado parcial
                } else {
                    result = currentInput.toDouble()
                }
                currentInput = ""
                operator = op
                pendingOperation = op
                updateDisplay("${formatResult(result)} $operator")
            } catch (e: Exception) {
                resetToZero() // Reseta para 0 em caso de erro
            }
        }
    }

    // Calcula o resultado final ou parcial
    private fun calculateResult() {
        if (currentInput.isNotEmpty() && operator.isNotEmpty()) {
            try {
                val secondOperand = currentInput.toDouble()
                result = when (operator) {
                    "+" -> result + secondOperand
                    "-" -> result - secondOperand
                    "×" -> result * secondOperand
                    "÷" -> if (secondOperand != 0.0) result / secondOperand else 0.0 // Retorna 0 em divisão por zero
                    else -> result
                }
                currentInput = formatResult(result)
                operator = ""
                pendingOperation = ""
                updateDisplay(currentInput)
            } catch (e: Exception) {
                resetToZero() // Reseta para 0 em caso de erro
            }
        } else if (operator.isEmpty() && currentInput.isEmpty()) {
            // Evita estado inválido após repetidas operações
            resetToZero()
        }
    }

    // Calcula a porcentagem
    private fun calculatePercent() {
        if (currentInput.isNotEmpty()) {
            try {
                val value = currentInput.toDouble() / 100
                currentInput = formatResult(value)
                updateDisplay(currentInput)
            } catch (e: Exception) {
                resetToZero() // Reseta para 0 em caso de erro
            }
        }
    }

    // Limpa tudo
    private fun clear() {
        currentInput = ""
        result = 0.0
        operator = ""
        pendingOperation = ""
        updateDisplay("0")
    }

    // Reseta para 0 em caso de problema
    private fun resetToZero() {
        currentInput = "0"
        result = 0.0
        operator = ""
        pendingOperation = ""
        updateDisplay("0")
    }

    // Remove o último caractere
    private fun backspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty() && operator.isNotEmpty()) {
                updateDisplay("${formatResult(result)} $operator")
            } else if (currentInput.isEmpty() && operator.isEmpty()) {
                updateDisplay("0")
            } else {
                updateDisplay(if (operator.isEmpty()) currentInput else "${formatResult(result)} $operator $currentInput")
            }
        }
    }

    // Atualiza o texto no display
    private fun updateDisplay(text: String) {
        displayTextView.setText(text.trim())
    }

    // Formata o resultado para remover .0 quando não necessário
    private fun formatResult(value: Double): String {
        return if (value.isNaN() || value.isInfinite()) "0" // Retorna 0 em casos extremos
        else if (value % 1 == 0.0) value.toLong().toString() // Remove .0 se for inteiro
        else String.format("%.6f", value).trimEnd('0').trimEnd('.') // Mantém decimais sem excesso
    }
}