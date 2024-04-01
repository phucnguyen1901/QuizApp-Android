package com.example.quizapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityQuizBinding
import android.util.Log;
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.quizapp.databinding.ScoreScreenBinding


class QuizActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var questionModelList: List<QuestionModel> = emptyList()
        var time = "3"
    }

    private lateinit var binding: ActivityQuizBinding
    private var currentQuestionIndex: Int = 0
    private var answer: String = ""
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpOnClick()
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeMillis, 1000L) {
            override fun onTick(p0: Long) {
                var seconds = p0 / 1000
                var minutes = seconds / 60
                var remainingSecond = seconds % 60
                binding.timer.text = String.format("%02d:%02d", minutes, remainingSecond)
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }

        }
    }

    private fun loadQuestions() {
        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextView.text =
                "Question ${(currentQuestionIndex + 1)} / ${questionModelList.size}"
            progressIndicator.progress = (currentQuestionIndex + 1) * 100 / questionModelList.size
            quizQuestion.text = questionModelList[currentQuestionIndex].question
            val answersTextViews = listOf(answer1, answer2, answer3, answer4)

            for ((index, textView) in answersTextViews.withIndex()) {
                textView.text = questionModelList[currentQuestionIndex].answers[index]
            }

        }
    }

    override fun onClick(p0: View?) {
        binding.apply {
            val answersTextViews = listOf(answer1, answer2, answer3, answer4)
            for (textView in answersTextViews) {
                textView.setBackgroundColor(getColor(R.color.orange))
            }
        }

        var clickBtn = p0 as Button
        if(clickBtn.id == R.id.next_btn){
            updateScore()
            currentQuestionIndex++
            loadQuestions()
        }else{
            answer = clickBtn.text.toString()
            clickBtn.setBackgroundColor(getColor(R.color.blue))
        }
    }

    private fun updateScore() {
        if(answer == questionModelList[currentQuestionIndex].correct){
            score++
        }
    }

    private fun setUpOnClick(){
        binding.apply {
            nextBtn.setOnClickListener(this@QuizActivity)
            val answersTextViews = listOf(answer1, answer2, answer3, answer4)
            for(textView in answersTextViews){
                textView.setOnClickListener(this@QuizActivity)
            }
        }
    }

    fun finishQuiz(){
        var percentage = (score.toFloat() / questionModelList.size.toFloat() * 100).toInt()
        var dialog = ScoreScreenBinding.inflate(layoutInflater)
        dialog.apply {
            progressIndicator.progress = percentage
            percentText.text = "$percentage %"
            totalQuestionsCorrect.text = "$score of ${questionModelList.size} are correct"
            finishBtn.setOnClickListener {
                finish()
            }
        }
        AlertDialog.Builder(this).setView(dialog.root).setCancelable(false).show()
    }

}