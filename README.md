### Explicação do Código por Partes

---

### 1. **Declaração do Pacote e Imports**
```kotlin
package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
```
- **O que faz**: 
  - O `package com.example.calculator` define o pacote onde o arquivo está localizado. Você deve ajustar isso para o pacote correto do seu projeto.
  - Os `imports` trazem as classes necessárias do Android:
    - `Bundle`: Usado para passar dados entre atividades.
    - `Button` e `EditText`: Componentes da interface (botões e campo de texto).
    - `AppCompatActivity`: Classe base para atividades compatíveis com versões mais antigas do Android.

---

### 2. **Declaração da Classe**
```kotlin
class MainActivity : AppCompatActivity() {
```
- **O que faz**: 
  - Define a classe `MainActivity`, que herda de `AppCompatActivity`. Isso significa que essa classe é uma atividade (tela) no aplicativo Android.

---

### 3. **Variáveis de Instância**
```kotlin
private lateinit var displayTextView: EditText
private var currentInput = ""
private var result = 0.0
private var operator = ""
private var pendingOperation = ""
```
- **O que faz**: 
  - Essas variáveis armazenam o estado da calculadora:
    - **`displayTextView`**: Um `EditText` (campo de texto) onde o usuário vê os números e operações. O `lateinit` indica que será inicializado depois (no `onCreate`).
    - **`currentInput`**: Uma `String` que guarda o número que o usuário está digitando agora (ex.: "123").
    - **`result`**: Um `Double` que armazena o resultado acumulado das operações (ex.: 10.5).
    - **`operator`**: Uma `String` que guarda o operador atual (ex.: "+", "-", "×", "÷").
    - **`pendingOperation`**: Uma `String` que rastreia a operação pendente para exibição.

---

### 4. **Método `onCreate`**
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    displayTextView = findViewById(R.id.displayTextView)

    // Vincula os botões...
}
```
- **O que faz**: 
  - Esse é o método principal que inicializa a atividade quando ela é criada:
    - `super.onCreate`: Chama o método da classe pai.
    - `setContentView(R.layout.activity_main)`: Define o layout da tela como o arquivo `activity_main.xml`.
    - `displayTextView = findViewById(R.id.displayTextView)`: Conecta o `EditText` do layout à variável `displayTextView`.
    - Depois disso, os botões (como `btn0`, `btnAdd`, etc.) são vinculados aos seus IDs no layout usando `findViewById` e configurados com listeners (explicado na próxima seção).

---

### 5. **Configuração dos Listeners para Botões**
```kotlin
val numberButtons = listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
numberButtons.forEach { button ->
    button.setOnClickListener {
        appendNumber(button.text.toString())
    }
}

btnDot.setOnClickListener { appendDot() }
btnAdd.setOnClickListener { setOperator("+") }
btnSubtract.setOnClickListener { setOperator("-") }
btnMultiply.setOnClickListener { setOperator("×") }
btnDivide.setOnClickListener { setOperator("÷") }
btnPercent.setOnClickListener { calculatePercent() }
btnEquals.setOnClickListener { calculateResult() }
btnClear.setOnClickListener { clear() }
btnBackspace.setOnClickListener { backspace() }
```
- **O que faz**: 
  - Configura o que acontece quando cada botão é clicado:
    - **Números (0-9)**: Uma lista de botões numéricos é criada e, para cada um, o método `appendNumber` é chamado com o texto do botão (ex.: "5").
    - **Ponto (.)**: Chama `appendDot` para adicionar um ponto decimal.
    - **Operadores (+, -, ×, ÷)**: Chama `setOperator` com o operador correspondente.
    - **Porcentagem (%)**: Chama `calculatePercent`.
    - **Igual (=)**: Chama `calculateResult` para calcular o resultado final.
    - **Limpar (C)**: Chama `clear` para resetar tudo.
    - **Backspace (⌫)**: Chama `backspace` para apagar o último dígito.

---

### 6. **Método `appendNumber`**
```kotlin
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
```
- **O que faz**: 
  - Adiciona um número ao `currentInput`:
    - Se `currentInput` é "0" e não há operador, substitui "0" pelo novo número (ex.: "0" vira "5").
    - Se há uma operação pendente (`pendingOperation`), começa um novo número e limpa `pendingOperation`.
    - Caso contrário, adiciona o número ao final de `currentInput` (ex.: "12" + "3" = "123").
  - **Atualiza o display**: Mostra apenas `currentInput` se não houver operador, ou a equação completa (`result operator currentInput`) se houver.

---

### 7. **Método `appendDot`**
```kotlin
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
```
- **O que faz**: 
  - Adiciona um ponto decimal ao `currentInput` se ele ainda não tiver um:
    - Se `currentInput` está vazio ou há uma operação pendente, começa com "0.".
    - Adiciona "." ao final de `currentInput` (ex.: "12" vira "12.").
  - **Atualiza o display**: Similar ao `appendNumber`.

---

### 8. **Método `setOperator`**
```kotlin
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
            resetToZero()
        }
    }
}
```
- **O que faz**: 
  - Define o operador para a próxima operação:
    - Se há um `currentInput`, verifica se já existe um operador:
      - Se sim, calcula o resultado parcial chamando `calculateResult`.
      - Se não, converte `currentInput` para `Double` e armazena em `result`.
    - Limpa `currentInput`, define o novo `operator` e `pendingOperation`, e atualiza o display com "`result operator`".
  - **Erro**: Se algo falhar (ex.: conversão inválida), reseta tudo com `resetToZero`.

---

### 9. **Método `calculateResult`**
```kotlin
private fun calculateResult() {
    if (currentInput.isNotEmpty() && operator.isNotEmpty()) {
        try {
            val secondOperand = currentInput.toDouble()
            result = when (operator) {
                "+" -> result + secondOperand
                "-" -> result - secondOperand
                "×" -> result * secondOperand
                "÷" -> if (secondOperand != 0.0) result / secondOperand else 0.0
                else -> result
            }
            currentInput = formatResult(result)
            operator = ""
            pendingOperation = ""
            updateDisplay(currentInput)
        } catch (e: Exception) {
            resetToZero()
        }
    } else if (operator.isEmpty() && currentInput.isEmpty()) {
        resetToZero()
    }
}
```
- **O que faz**: 
  - Calcula o resultado da operação:
    - Converte `currentInput` para `Double` (segundo operando).
    - Usa um `when` para aplicar a operação correta (+, -, ×, ÷) entre `result` e o segundo operando.
    - Para divisão, retorna 0.0 se o divisor for zero.
    - Armazena o resultado em `currentInput` (formatado), limpa `operator` e `pendingOperation`, e atualiza o display.
  - **Erro**: Reseta para "0" se houver falha.
  - **Estado inválido**: Reseta para "0" se não houver operador nem entrada.

---

### 10. **Método `calculatePercent`**
```kotlin
private fun calculatePercent() {
    if (currentInput.isNotEmpty()) {
        try {
            val value = currentInput.toDouble() / 100
            currentInput = formatResult(value)
            updateDisplay(currentInput)
        } catch (e: Exception) {
            resetToZero()
        }
    }
}
```
- **O que faz**: 
  - Calcula a porcentagem do `currentInput`:
    - Divide o valor por 100 (ex.: "50" vira "0.5").
    - Formata o resultado e atualiza o display.
  - **Erro**: Reseta para "0" se houver falha.

---

### 11. **Método `clear`**
```kotlin
private fun clear() {
    currentInput = ""
    result = 0.0
    operator = ""
    pendingOperation = ""
    updateDisplay("0")
}
```
- **O que faz**: 
  - Reseta todas as variáveis para seus valores iniciais e define o display como "0".

---

### 12. **Método `resetToZero`**
```kotlin
private fun resetToZero() {
    currentInput = "0"
    result = 0.0
    operator = ""
    pendingOperation = ""
    updateDisplay("0")
}
```
- **O que faz**: 
  - Similar ao `clear`, mas garante que `currentInput` seja "0". Usado em situações de erro.

---

### 13. **Método `backspace`**
```kotlin
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
```
- **O que faz**: 
  - Remove o último caractere de `currentInput`:
    - Se `currentInput` fica vazio e há um operador, mostra "`result operator`".
    - Se `currentInput` fica vazio e não há operador, mostra "0".
    - Caso contrário, atualiza o display com a equação ou apenas `currentInput`.

---

### 14. **Método `updateDisplay`**
```kotlin
private fun updateDisplay(text: String) {
    displayTextView.setText(text.trim())
}
```
- **O que faz**: 
  - Define o texto do `EditText` (`displayTextView`) com o valor fornecido, removendo espaços extras.

---

### 15. **Método `formatResult`**
```kotlin
private fun formatResult(value: Double): String {
    return if (value.isNaN() || value.isInfinite()) "0"
    else if (value % 1 == 0.0) value.toLong().toString()
    else String.format("%.6f", value).trimEnd('0').trimEnd('.')
}
```
- **O que faz**: 
  - Formata um `Double` para uma `String` limpa:
    - Retorna "0" se o valor for `NaN` (não é um número) ou `Infinite`.
    - Se o valor não tem decimais (ex.: 5.0), converte para inteiro (ex.: "5").
    - Caso contrário, formata com até 6 casas decimais e remove zeros à direita e o ponto se desnecessário (ex.: "2.5000" vira "2.5").

---

### Resumo Geral
- **Como funciona**: 
  - O usuário digita números com `appendNumber`, adiciona operadores com `setOperator`, e calcula resultados com `calculateResult`.
  - As variáveis (`currentInput`, `result`, `operator`, `pendingOperation`) mantêm o estado da calculadora.
  - O display é atualizado continuamente com `updateDisplay`.
- **Tratamento de erros**: Exceções (como divisão por zero ou conversão inválida) são tratadas resetando para "0".
- **Formatação**: `formatResult` garante que os números sejam exibidos de forma clara e sem decimais desnecessários.

Esse código cria uma calculadora básica que suporta operações contínuas, números decimais e mantém o display sempre válido. Se quiser mais detalhes ou adicionar funcionalidades, é só me avisar!
