package com.hfad.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider

class GameFragment : Fragment() {
    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        viewModel.gameOver.observe(viewLifecycleOwner, Observer {
                newValue ->
            if (newValue) {
                val action = GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view?.findNavController()?.navigate(action)
            }
        })

        val composeView = ComposeView(requireContext())

        composeView.setContent {
            MaterialTheme {
                Surface {
                    GameFragmentContent(viewModel)
                }
            }
        }

        return composeView
    }
}

@Composable
fun FinishGameButton(clicked: () -> Unit) {
    Button(
        onClick = clicked
    ) {
        Text("Finish Game")
    }
}

@Composable
fun EnterGuess(guess: String, changed: (String) -> Unit) {
    TextField(
        value = guess,
        label = { Text("Guess a latter") },
        onValueChange = changed
    )
}

@Composable
fun GuessButton(clicked: () -> Unit) {
    Button(
        onClick = clicked
    ) {
        Text("Guess!")
    }
}

@Composable
fun IncorrectGuessesText(viewModel: GameViewModel) {
    val incorrectGuesses = viewModel.incorrectGuesses.observeAsState()

    incorrectGuesses.value?.let {
        Text(stringResource(R.string.incorrect_guesses, it))
    }
}

@Composable
fun LivesLeftText(viewModel: GameViewModel) {
    val livesLeft = viewModel.livesLeft.observeAsState()

    livesLeft.value?.let {
        Text(stringResource(R.string.lives_left, it))
    }
}

@Composable
fun SecretWorldDisplay(viewModel: GameViewModel) {
    val display = viewModel.secretWordDisplay.observeAsState()
    display.value?.let {
        Text(
            text = it,
            letterSpacing = 0.1.em,
            fontSize = 36.sp
        )
    }
}

@Composable
fun GameFragmentContent(viewModel: GameViewModel) {
    val guess = remember { mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            SecretWorldDisplay(viewModel)
        }

        LivesLeftText(viewModel)
        IncorrectGuessesText(viewModel)
        EnterGuess(guess.value) { guess.value = it }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GuessButton {
                viewModel.makeGuess(guess.value.uppercase())
                guess.value = ""
            }

            FinishGameButton {
                viewModel.finishGame()
            }
        }
    }
}