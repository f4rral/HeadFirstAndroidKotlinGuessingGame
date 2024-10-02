package com.hfad.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hfad.guessinggame.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    // Список возможных слов. В реальном приложении список должен
    // быть более длинным, чтобы игра не была слишком простой
    val words = listOf("Android", "Activity", "Fragment")

    val secretWord = words.random().uppercase()     // Слово, которое нужно угадать
    var secretWordDisplay = ""                      // Вид, в котором слово отображается на экране
    var correctGuesses = ""                         // Количество правильных предположений
    var incorrectGuesses = ""                       // Количество неправильных предположений
    var livesLeft = 8                               // Количество оставшихся жизней

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        secretWordDisplay = deriveSecretWordDisplay()
        updateScreen()

        binding.guessButton.setOnClickListener() {
            makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null
            updateScreen()

            if (isWon() || isLost()) {
                val action = GameFragmentDirections.actionGameFragmentToResultFragment(wonLostMessage())
                view.findNavController().navigate(action)
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateScreen() {
        binding.word.text = secretWordDisplay
        binding.lives.text = "You have $livesLeft lives left."
        binding.incorrectGuesses.text = "Incorrect guesses: $incorrectGuesses"
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

    // Вызывается каждый раз, когда пользователь вводит предположение
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

    // возвращает строку, которая сообщает,
    // выиграл или проиграл пользователь и какое слово было загадано
    fun wonLostMessage() : String {
        var message = ""

        if (isWon()) message = "You won!"
        else if (isLost()) message = "You lost!"

        message += " The word was $secretWord."

        return message
    }
}