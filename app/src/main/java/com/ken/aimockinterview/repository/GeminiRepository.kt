package com.ken.aimockinterview.repository

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.ken.aimockinterview.models.QuesAnsModel
import com.ken.aimockinterview.models.ResumeData
import com.ken.aimockinterview.models.UserRatingFeedback
import com.ken.aimockinterview.models.wrapper.GeminiQuestionsResp
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException

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
            - **Difficulty Level**: 4 Easy Questions, 3 Medium and 3 Hard Questions and do not mention its difficulty level with questions.
            
            For each question and answer, please consider the candidate having $experienceYears year(s) of experience. 
            Make sure the answers are concise and to the point.

            Return the data in JSON format with "questions" as the key containing an array of objects with "question" and "answer" fields.
        """.trimIndent()

        try {
            val response = genAi.startChat().sendMessage(prompt = prompt)
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
        $question
        
        User's Answer:
        $userAnswer

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
            val jsonString = response.text?.trim()?.removeSurrounding("```json", "```")
//            response.text!!.removePrefix("```json").trim().removeSuffix("```").toString()
            if (!jsonString.isNullOrEmpty()) {
                /*                response.text?.let { text ->
                                    val cleanedResult = text.replace("```json", "")
                                        .replace("```", "").trim()*/
                Log.d(Constants.TAG, "Final Cleaned Result: $jsonString")
                convertFromJson(jsonString)?.let { feedback ->
                    Log.d(Constants.TAG, "Feedback Result: $feedback")
                    _userRatingFeedback.value = feedback
                }
            }
//        }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error: ${e.message}")
            throw e
        }
    }

    suspend fun extractUserResume(
        resumeDetails: String,
    ) {
        try {

            /*  val prompt = """
                    you are an AI  bot designed to act as a professional for parsing resumes.
                    Your are given with resume and your job is to extract the following:

                    1. Job Role (extract only the latest or most relevant roles) use field name as jobRole.
                    2. Technical Skills (list programming languages, frameworks, tools, etc.) use field name as technicalSkills.
                    3. Soft Skills (list relevant interpersonal and problem-solving skills) use field name as softSkills.
                    4. Total Months Of Experience include all months those are given and if there is PRESENT in epeirence then consider it as a current year with current month
                    5. experienceDetails – Provide a JSON array with the total months of experience for each job separately.

                    Make sure:
                        - Job roles should be **distinct** (avoid listing sub-roles separately).
                        - Experience calculation should **not double-count overlapping dates**.
                        - Format the output in **valid JSON only**.

                    Here is the resume content:

                    $resumeDetails
                    """.trimIndent()*/
            val currentDate = Utils.getCurrentDate()
            Log.d(Constants.TAG, "Current Date: $currentDate")
            val prompt = """

            You are an AI bot designed to act as a professional resume parser.
             Your task is to extract specific details from the provided resume content.
              Use your current date (as $currentDate) for any calculations involving "Present" in the work experience. Extract the following key details:
            
            1. Job Role:
                Extract only the latest or most relevant job role (based on the most recent start date or "Present" status).
                Use the field name jobRole.
                Ensure the role is distinct (do not list sub-roles or variations separately).
               
           2. Technical Skills:
                Compile a list of all programming languages, frameworks, tools, databases, and other technical skills mentioned across the resume.
                Use the field name technicalSkills.
                Avoid duplicates in the list.
                 
           3. Soft Skills:
                Identify and list relevant interpersonal, communication, or problem-solving skills (e.g., teamwork, leadership, stakeholder management) explicitly mentioned or implied in the resume.
                Use the field name softSkills.
                Avoid duplicates and generic assumptions not supported by the text.
           4. Total Months of Each Experience:
                For each work experience, calculate the duration in months based on the start and end dates.
                If "Present" is mentioned, calculate up to $currentDate.
                Please consider every month lets example(march 2024 - December 2024 then its total is 10 months so please consider every month)
                Include this as part of an array under the field name experienceDetails, with each entry containing the job role, company, duration in months, and date range.
           5. Total Years of Experience:
                  Sum all the duration in months of each experience.
                  Convert months to years (1 year = 12 months) and round the result to 1 decimal place.
//                Calculate the total years of professional work experience based only on work experience sections (exclude projects, education, or non-professional roles).
//                Sum only non-overlapping experience durations (if dates overlap, count only the unique time period).
//                Convert months to years (1 year = 12 months) and round the result to 1 decimal place.
//                Use the field name yearsOfExperience.   
            
            Requirements:
           
            Ensure job roles are distinct and not repeated unnecessarily.
            Avoid double-counting overlapping experience dates in the total years calculation.
            Format the output in valid JSON only
           
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
        val response = gson.fromJson(jsonString, GeminiQuestionsResp::class.java)
        response?.questions ?: emptyList()  // Ensure safe access
    } catch (e: JsonSyntaxException) {
        Log.e(Constants.TAG, "JSON Parsing error: ${e.message}")
        emptyList()
    } catch (e: Exception) {
        Log.e(Constants.TAG, "Unexpected error: ${e.message}")
        emptyList()
    }
}

private fun convertFromJson(jsonString: String): UserRatingFeedback? {
    return try {
        Gson().fromJson(jsonString, UserRatingFeedback::class.java)
    } catch (e: JsonSyntaxException) {
        Log.e(Constants.TAG, "JSON parsing error: ${e.message}")
        null
    } catch (e: IOException) {
        Log.e(Constants.TAG, "IO error: ${e.message}")
        null
    } catch (e: Exception) {
        Log.e(Constants.TAG, "Unexpected error: ${e.message}")
        null
    }
}

private fun convertResumeData(jsonString: String): ResumeData? {
    return try {
        Gson().fromJson(jsonString, ResumeData::class.java)
    } catch (e: JsonSyntaxException) {
        Log.e(Constants.TAG, "JSON parsing error: ${e.message}")
        null
    } catch (e: IOException) {
        Log.e(Constants.TAG, "IO error: ${e.message}")
        null
    } catch (e: Exception) {
        Log.e(Constants.TAG, "Unexpected error: ${e.message}")
        null
    }
}

/*

you are an AI  bot designed to act as a professional for parsing resumes.
Your are given with resume and your job is to extract the following:

1. Job Role (extract only the latest or most relevant roles) use field name as jobRole.
2. Technical Skills (list programming languages, frameworks, tools, etc.) use field name as technicalSkills.
3. Soft Skills (list relevant interpersonal and problem-solving skills) use field name as softSkills.
4. Date, month or years of experience given according to the resume

Make sure:
- Job roles should be **distinct** (avoid listing sub-roles separately).
- Experience calculation should **not double-count overlapping dates**.
- Format the output in **valid JSON only**.

Here is the resume content:*/

/* 4. Total Years of Experience:
                  - calculate the total years of professional work experience ONLY. Exclude projects and education.
                  - Sum only **non-overlapping** experience durations.
                  - **Round to 1 decimal place** for accuracy
                  - use field yearsOfExperience
                  - If the experience is listed in **months**, convert it to **years (1 year = 12 months)**.*/