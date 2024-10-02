package com.hfad.guessinggame

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // Список возможных слов. В реальном приложении список должен
    // быть более длинным, чтобы игра не была слишком простой
    val words = listOf("Android", "Activity", "Fragment")

    val secretWord = words.random().uppercase()     // Слово, которое нужно угадать
    var secretWordDisplay = ""                      // Вид, в котором слово отображается на экране
    var correctGuesses = ""                         // Количество правильных предположений
    var incorrectGuesses = ""                       // Количество неправильных предположений
    var livesLeft = 8                               // Количество оставшихся жизней

    init {
        // Этот метод вызывается в блоке init, который выполняется при инициализации класса.
        secretWordDisplay = deriveSecretWordDisplay()
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
                secretWordDisplay = deriveSecretWordDisplay()
            } else {
                incorrectGuesses += "$guess "
                livesLeft--
            }
        }
    }

    // Пользователь выиграл, если загаданное слово совпадает с secretWordDisplay
    fun isWon() = secretWord.equals(secretWordDisplay, true)

    // Пользователь проиграл, если у него кончились жизни
    fun isLost() = livesLeft <= 0

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