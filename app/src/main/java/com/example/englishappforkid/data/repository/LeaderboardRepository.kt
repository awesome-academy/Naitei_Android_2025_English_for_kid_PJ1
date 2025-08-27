package com.example.englishappforkid.data.repository

import com.example.englishappforkid.data.model.LeaderboardEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class LeaderboardRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /** Lấy Top 10 + người chơi hiện tại nếu không có trong Top 10 */
    suspend fun getLeaderboardData(): Triple<List<LeaderboardEntry>, List<LeaderboardEntry>, LeaderboardEntry?> {
        val currentUserId = auth.currentUser?.uid ?: return Triple(emptyList(), emptyList(), null)

        return try {
            // 1️⃣ Lấy Top 10 từ leaderboard
            val topSnapshot =
                db
                    .collection("leaderboard")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()

            val topList = mutableListOf<LeaderboardEntry>()

            for (doc in topSnapshot.documents) {
                val userId = doc.id
                val userProfile =
                    db
                        .collection("user_profiles")
                        .document(userId)
                        .get()
                        .await()

                val entry =
                    LeaderboardEntry(
                        rank = 0,
                        name = userProfile.getString("fullname") ?: "Unknown",
                        isYou = userId == currentUserId,
                        score = doc.getLong("score")?.toInt() ?: 0,
                        avatar = userProfile.getString("avatarUrl") ?: "",
                    )
                topList.add(entry)
            }

            var yourEntry: LeaderboardEntry? = null
            val youLeaderboardDoc =
                db
                    .collection("leaderboard")
                    .document(currentUserId)
                    .get()
                    .await()
            if (youLeaderboardDoc.exists()) {
                val youProfile =
                    db
                        .collection("user_profiles")
                        .document(currentUserId)
                        .get()
                        .await()
                yourEntry =
                    LeaderboardEntry(
                        rank = 0,
                        name = youProfile.getString("fullname") ?: "You",
                        isYou = true,
                        score = youLeaderboardDoc.getLong("score")?.toInt() ?: 0,
                        avatar = youProfile.getString("avatarUrl") ?: "",
                    )

                if (topList.none { it.isYou }) {
                    topList.add(yourEntry)
                }
            }

            // Tính rank
            val allScoresSnapshot =
                db
                    .collection("leaderboard")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .get()
                    .await()

            val sortedScores =
                allScoresSnapshot.documents
                    .map { it.getLong("score")?.toInt() ?: 0 }
                    .distinct()
                    .sortedDescending()

            val rankedList =
                topList.map { entry ->
                    val rank = sortedScores.indexOfFirst { it == entry.score } + 1
                    entry.copy(rank = rank)
                }

            val topThree = rankedList.take(3)
            val yourRank = yourEntry?.copy(rank = sortedScores.indexOfFirst { it == yourEntry.score } + 1)

            Triple(rankedList, topThree, yourRank)
        } catch (e: Exception) {
            Triple(emptyList(), emptyList(), null)
        }
    }
}
