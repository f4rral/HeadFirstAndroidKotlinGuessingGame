package com.hfad.guessinggame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // Список возможных слов. В реальном приложении список должен
    // быть более длинным, чтобы игра не была слишком простой
    private val words = listOf("Android", "Activity", "Fragment")

    // Слово, которое нужно угадать
    private val secretWord = words.random().uppercase()

    // Вид, в котором слово отображается на экране
    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String>
        get() = _secretWordDisplay

    // Количество правильных предположений
    private var correctGuesses = ""

    // Количество неправильных предположений
    private val _incorrectGuesses = MutableLiveData<String>("")
    val incorrectGuesses: LiveData<String>
        get() = _incorrectGuesses

    // Количество оставшихся жизней
    private val _livesLeft = MutableLiveData<Int>(8)
    val livesLeft: LiveData<Int>
        get() = _livesLeft

    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver: LiveData<Boolean>
        get() = _gameOver

    init {
        // Этот метод вызывается в блоке init, который выполняется при инициализации класса.
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }

    // Здесь создается строка с формой, в которой загаданное слово должно отображаться на экране
    private fun deriveSecretWordDisplay(): String {
        var display = ""

        secretWord.forEach {
            display += checkLetter(it.toString())
        }

        return display
    }

    // Проверяет, содержит ли загаданное слово букву, введенную пользователем
    // Если содержит, то возвращается буква, а если нет, возвращается «_»
    private fun checkLetter(str: String): String {
        return when (correctGuesses.contains(str)) {
            true -> str
            false -> "_"
        }
    }

    // Вызывается, когда пользователь вводит предположение
    fun makeGuess(guess: String) {
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {
                correctGuesses += guess
                _secretWordDisplay.value = deriveSecretWordDisplay()
            } else {
                _incorrectGuesses.value += "$guess "
                _livesLeft.value = _livesLeft.value?.minus(1)
            }

            if (isWon() || isLost()) _gameOver.value = true
        }
    }

    // Пользователь выиграл, если загаданное слово совпадает с secretWordDisplay
    private fun isWon() = secretWord.equals(_secretWordDisplay.value, true)

    // Пользователь проиграл, если у него кончились жизни
    private fun isLost() = (_livesLeft.value ?: 0) <= 0

    // Возвращает строку, которая сообщает,
    // выиграл или проиграл пользователь и какое слово было загадано
    fun wonLostMessage() : String {
        var message = ""

        if (isWon()) message = "You won!"
        else if (isLost()) message = "You lost!"

        message += " The word was $secretWord."

        return message
    }

    // Окончание игры
    fun finishGame() {
        _gameOver.value = true
    }
}