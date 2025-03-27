package com.ken.aimockinterview.repository

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.ken.aimockinterview.models.QuesAnsModel
import com.ken.aimockinterview.models.ResumeData
import com.ken.aimockinterview.models.UserRatingFeedback
import com.ken.aimockinterview.models.wrapper.GeminiQuestionsResp
import com.ken.aimockinterview.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GeminiRepository {

    private val genAi =
        GenerativeModel(
            modelName = Constants.GEMINI_MODEL,
            apiKey = Constants.GEMINI_API_KEY,
        )

    private val _userRatingFeedback = MutableStateFlow<UserRatingFeedback?>(null)
    val userRatingFeedback: StateFlow<UserRatingFeedback?>
        get() = _userRatingFeedback
    private val _resumeData = MutableStateFlow<ResumeData?>(null)
    val resumeData: StateFlow<ResumeData?>
        get() = _resumeData

    /* private val prompt =
         "    Generate 10 interview questions and model answers suitable for a candidate applying for an 'Android Developer' position. The candidate has 1 year of experience.\n" +
                 "\n" +
                 "    The job description emphasizes the following skills: Java, Kotlin, Jetpack Compose, Firebase, Retrofit, Glide, and RESTful APIs.\n" +
                 "\n" +
                 "    Please provide a mix of behavioral, technical, and situational questions. For each question, provide a sample answer that demonstrates a good understanding of the required skills and experience.\n" +
                 "\n" +
                 "    Specifically, address:\n" +
                 "\n" +
                 "    Technical Knowledge: Questions related to the technologies listed in the job description (Java, Kotlin, Jetpack Compose, Firebase, Retrofit, Glide, RESTful APIs).\n" +
                 "\n" +
                 "    Practical Experience: Questions that assess the candidate's hands-on experience with these technologies.\n" +
                 "\n" +
                 "    Behavioral Skills: Questions that evaluate the candidate's problem-solving abilities, teamwork, and communication skills.\n" +
                 "\n" +
                 "    Situational questions: Questions that test how the candidate would react to realistic work place scenarios.\n" +
                 "\n" +
                 "    For each question and answer, please consider the candidate having 1 year of experience. Be mindful of the experience level when generating questions and answers. Make sure the answers are concise and to the point.\n" +
                 "    Give Questions and Answered as field in JSON.\n"
 */

    suspend fun getQuestions(
        jobRole: String,
        jobDescription: String,
        experienceYears: String,
    ): List<QuesAnsModel> {
        val prompt = """
            Generate 10 interview questions and model answers suitable for a candidate applying for a '$jobRole' position. 
            The candidate has $experienceYears year(s) of experience.
            
            The job description emphasizes the following skills: $jobDescription.
            
            Please provide a mix of behavioral, technical, and situational questions. For each question, provide a sample answer 
            that demonstrates a good understanding of the required skills and experience.
            
            Specifically, address:

            - **Technical Knowledge**: Questions related to the technologies listed in the job description ($jobDescription).
            - **Practical Experience**: Questions that assess the candidate's hands-on experience with these technologies.
            - **Behavioral Skills**: Questions that evaluate the candidate's problem-solving abilities, teamwork, and communication skills.
            - **Situational Questions**: Questions that test how the candidate would react to realistic workplace scenarios.

            For each question and answer, please consider the candidate having $experienceYears year(s) of experience. 
            Make sure the answers are concise and to the point.

            Return the data in JSON format with "questions" as the key containing an array of objects with "question" and "answer" fields.
        """.trimIndent()

        try {
            val response = genAi.startChat().sendMessage(prompt = prompt)
//            val result = response.text!!.removePrefix("```json").trim().toString()
//            Log.d(Constants.TAG, "Questions: $result")
//            val questionJson = result.removeSuffix("```").trim().toString()
//            Log.d(Constants.TAG, "Questions JSON: $questionJson")
            Log.d(Constants.TAG, "Gemini Questions Response: ${response.text!!}")
            response.text?.let { text ->
                val cleanedResult = text.replace("```json", "")
                    .replace("```", "").trim()
                Log.d(Constants.TAG, "Final Cleaned Result: $cleanedResult")
                return convertJsonToList(cleanedResult)
            } ?: run {
                Log.e(Constants.TAG, "Response Text is Null")
                return emptyList()
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error: ${e.message}")
            throw e
        }
    }

    suspend fun getFeedback(
        question: String,
        userAnswer: String,
    ) {

        val prompt = """
        
        Evaluate the given interview question and user-provided answer. Provide a rating (out of 10) based on the relevance, depth, and clarity of the response. Additionally, provide constructive feedback on areas of improvement.

        Interview Question:
        {${question}}
        
        User's Answer:
        {${userAnswer}}

        Evaluation Criteria:
        Rate the answer based on the following aspects:

        Relevance (3 points): Does the answer directly address the question?

        Depth (3 points): Does the response demonstrate sufficient understanding and experience?

        Clarity (2 points): Is the answer well-structured and easy to understand?

        Conciseness (2 points): Is the answer to the point without unnecessary details?

        Expected Output Format (JSON):
        {
          "rating": <integer (1-10)>,
          "feedback": "<brief constructive feedback in 3-5 lines>"
        }
       
    """.trimIndent()

        try {
            val response = genAi.startChat().sendMessage(prompt = prompt)
            response.text!!.removePrefix("```json").trim().removeSuffix("```").toString()
            response.text?.let { text ->
                val cleanedResult = text.replace("```json", "")
                    .replace("```", "").trim()
                Log.d(Constants.TAG, "Final Cleaned Result: $cleanedResult")
                convertFromJson(cleanedResult)?.let { feedback ->
                    Log.d(Constants.TAG, "Feedback Result: $feedback")
                    _userRatingFeedback.value = feedback
                }
            }
//            val feedback = convertFromJson(result)
//            _userRatingFeedback.value = feedback
//            Log.d(Constants.TAG, "Feedback Response: $feedback")
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error: ${e.message}")
            throw e
        }
    }

    suspend fun extractUserResume(
        resumeDetails: String,
    ) {
        try {

            val prompt = """
                  you are an AI  bot designed to act as a professional for parsing resumes.
                  Your are given with resume and your job is to extract the following:

                  1. Job Role (extract only the latest or most relevant roles) use field name as jobRole.
                  2. Technical Skills (list programming languages, frameworks, tools, etc.) use field name as technicalSkills.
                  3. Soft Skills (list relevant interpersonal and problem-solving skills) use field name as softSkills.
                  4. Total Years of Experience:
                   - calculate the total years of professional work experience ONLY. Exclude projects and education.
                   - Sum only **non-overlapping** experience durations.
                   - **Round to 1 decimal place** for accuracy
                   - use field yearsOfExperience
                   - If the experience is listed in **months**, convert it to **years (1 year = 12 months)**.

                  Make sure:
                      - Job roles should be **distinct** (avoid listing sub-roles separately).
                      - Experience calculation should **not double-count overlapping dates**.
                      - Format the output in **valid JSON only**.

                  Here is the resume content:

                  $resumeDetails
                  """.trimIndent()

            val response = genAi.startChat().sendMessage(prompt = prompt)
            Log.d(Constants.TAG, "Resume Response: ${response.text!!}")
            response.text?.let { text ->
                val clearedJson = text.replace("```json", "")
                    .replace("```", "").trim()
                Log.d(Constants.TAG, "Extracted Result: $clearedJson")
                convertResumeData(clearedJson)?.let { data ->
                    Log.d(Constants.TAG, "Resume Data: $data")
                    _resumeData.value = data
                }
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "error: ${e.message}")
        }
    }

}

private fun convertJsonToList(jsonString: String): List<QuesAnsModel> {
    return try {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, GeminiQuestionsResp::class.java)
        jsonObject.questions
    } catch (e: Exception) {
        Log.e(Constants.TAG, "Error: ${e.message}")
        emptyList()
    }
}

private fun convertFromJson(jsonString: String): UserRatingFeedback? {
    return try {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, UserRatingFeedback::class.java)
        jsonObject
    } catch (e: Exception) {
        Log.e(Constants.TAG, "Error: ${e.message}")
        null
    }
}

private fun convertResumeData(jsonString: String): ResumeData? {
    return try {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, ResumeData::class.java)
        jsonObject
    } catch (e: Exception) {
        Log.e(Constants.TAG, "Error: ${e.message}")
        null
    }
}