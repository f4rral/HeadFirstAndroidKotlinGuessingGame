package com.hfad.guessinggame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // Список возможных слов. В реальном приложении список должен
    // быть более длинным, чтобы игра не была слишком простой
    val words = listOf("Android", "Activity", "Fragment")

    // Слово, которое нужно угадать
    val secretWord = words.random().uppercase()

    // Вид, в котором слово отображается на экране
    val secretWordDisplay = MutableLiveData<String>()

    // Количество правильных предположений
    var correctGuesses = ""

    // Количество неправильных предположений
    val incorrectGuesses = MutableLiveData<String>("")

    // Количество оставшихся жизней
    val livesLeft = MutableLiveData<Int>(8)

    init {
        // Этот метод вызывается в блоке init, который выполняется при инициализации класса.
        secretWordDisplay.value = deriveSecretWordDisplay()
    }

    // Здесь создается строка с формой, в которой загаданное слово должно отображаться на экране
    fun deriveSecretWordDisplay(): String {
        var display = ""

        secretWord.forEach {
            display += checkLetter(it.toString())
        }

        return display
    }

    // Проверяет, содержит ли загаданное слово букву, введенную пользователем
    // Если содержит, то возвращается буква, а если нет, возвращается «_»
    fun checkLetter(str: String): String {
        return when (correctGuesses.contains(str)) {
            true -> str
            false -> "_"
        }
    }

    fun makeGuess(guess: String) {
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {
                correctGuesses += guess
                secretWordDisplay.value = deriveSecretWordDisplay()
            } else {
                incorrectGuesses.value += "$guess "
                livesLeft.value = livesLeft.value?.minus(1)
            }
        }
    }

    // Пользователь выиграл, если загаданное слово совпадает с secretWordDisplay
    fun isWon() = secretWord.equals(secretWordDisplay.value, true)

    // Пользователь проиграл, если у него кончились жизни
    fun isLost() = (livesLeft.value ?: 0) <= 0

    // Возвращает строку, которая сообщает,
    // выиграл или проиграл пользователь и какое слово было загадано
    fun wonLostMessage() : String {
        var message = ""

        if (isWon()) message = "You won!"
        else if (isLost()) message = "You lost!"

        message += " The word was $secretWord."

        return message
    }
}