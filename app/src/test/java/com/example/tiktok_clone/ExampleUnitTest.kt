package com.example.tiktok_clone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
//    fun testUIState() {
//        var messageText by mutableStateOf("")
//        println("Initial: '$messageText'")
//        messageText = "Hello"
//        println("After typing: '$messageText'")
//        messageText = ""
//        println("After clear: '$messageText'")
//    }

//    data class Comment(val id: String, val parentId: String?, val content: String)
//
//    @Test
//    fun testGroupComments() {
//        val comments = listOf(
//            Comment("1", null, "Root 1"),
//            Comment("2", "1", "Reply to 1"),
//            Comment("3", null, "Root 2"),
//            Comment("4", "1", "Another reply to 1")
//        )
//        val repliesByParent = comments
//            .filter { it.parentId != null }
//            .groupBy({ it.parentId!! }, { it })
//
//        println("Replies for comment 1: ${repliesByParent["1"]?.size ?: 0}")
//        println("Replies for comment 2: ${repliesByParent["2"]?.size ?: 0}")
//    }

    enum class MessageType { TEXT, IMAGE, VIDEO }

    fun renderMessage(type: MessageType): String = when (type) {
        MessageType.TEXT -> "Text message"
        MessageType.IMAGE -> "Image message"
        MessageType.VIDEO -> "Video message"
    }

    @Test
    fun testRenderMessage() {
        println(renderMessage(MessageType.TEXT))
        println(renderMessage(MessageType.VIDEO))
    }

    data class Comment(
        val id: String,
        val content: String,
        val likeCount: Long = 0,
        val isLiked: Boolean = false
    )

    @Test
    fun testCopy() {
        val c = Comment(id = "c1", content = "Hay quá!", likeCount = 5)
        val liked = c.copy(isLiked = true, likeCount = c.likeCount + 1)
        println(c)
        println(liked)
    }

    sealed interface UIEvent {
        data class LikePost(val postId: String) : UIEvent
        data class LoadMore(val page: Int) : UIEvent
        object Refresh : UIEvent
    }

    fun handleEvent(event: UIEvent) {
        when (event) {
            is UIEvent.LikePost -> println("Liking post: ${event.postId}")
            is UIEvent.LoadMore -> println("Loading page: ${event.page}")
            is UIEvent.Refresh -> println("Refreshing...")
        }
    }

    @Test
    fun testHandleEvent() {
        handleEvent(UIEvent.LikePost("post_123"))
        handleEvent(UIEvent.LoadMore(2))
    }

    fun getAvatarUrl(url: String?): String {
        return url?.let { "https://cdn.example.com/$it" } ?: "default_avatar.png"
    }

    @Test
    fun testAvatarUrl() {
        println(getAvatarUrl("user123.jpg"))
        println(getAvatarUrl(null))
    }

//    data class User(val id: String, val name: String)
//
//    @Test
//    fun testFilterFriends() {
//        val allUsers = listOf(
//            User("1", "Alice"),
//            User("2", "Bob"),
//            User("3", "Charlie")
//        )
//        val currentUserId = "2"
//        val friends = allUsers.filter { it.id != currentUserId }
//        friends.forEach { println(it.name) }
//    }

    fun processMessage(content: String?) {
        content?.let {
            println("Processing: $it")
            println("Length: ${it.length}")
        } ?: println("Message is null")
    }

    @Test
    fun testProcessMessage() {
        processMessage("Hello")
        processMessage(null)
    }

    fun Long.toShortString(): String = when {
        this >= 1_000_000 -> "${this / 1_000_000}Tr"
        this >= 1_000 -> "${this / 1_000}N"
        else -> this.toString()
    }

    @Test
    fun testExtension() {
        println(1500L.toShortString())
        println(2_000_000L.toShortString())
    }

    enum class MessageStatus { SENDING, SENT, DELIVERED, SEEN }

    fun statusText(status: MessageStatus): String = when (status) {
        MessageStatus.SENDING -> "Đang gửi"
        MessageStatus.SENT -> "Đã gửi"
        MessageStatus.DELIVERED -> "Đã nhận"
        MessageStatus.SEEN -> "Đã xem"
    }

    @Test
    fun testStatusText() {
        println(statusText(MessageStatus.SENDING))
        println(statusText(MessageStatus.SENT))
    }

    sealed class UIState {
        object Loading : UIState()
        data class Success(val data: String) : UIState()
        data class Error(val msg: String) : UIState()
    }

    fun render(state: UIState): String = when (state) {
        is UIState.Loading -> "Loading..."
        is UIState.Success -> "Data: ${state.data}"
        is UIState.Error -> "Error: ${state.msg}"
    }

    @Test
    fun testUIState() {
        println(render(UIState.Loading))
        println(render(UIState.Success("Hello")))
    }

    sealed class ResultState {
        object Success : ResultState()
        data class Error(val msg: String) : ResultState()
    }

    suspend fun fetchData(): ResultState {
        return try {
            // simulate network
            if (true) throw Exception("Network timeout")
            ResultState.Success
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown")
        }
    }

    @Test
    fun testFetch() = runBlocking {
        val result = fetchData()
        when (result) {
            is ResultState.Success -> println("Success")
            is ResultState.Error -> println("Error: ${result.msg}")
        }
    }

    @Test
    fun testLaunchedEffect() = runBlocking {
        var counter = 0
        launch {
            delay(100)
            counter = 1
        }
        delay(200)
        println("Counter: $counter")
    }

    data class FollowNotificationDto(
        val id: String?,
        val followerId: String?,
        val createdAt: Long?
    )
    data class FollowNotification(
        val id: String,
        val followerId: String,
        val createdAt: Long
    )
    fun FollowNotificationDto.toModel(): FollowNotification = FollowNotification(
        id = id ?: "",
        followerId = followerId ?: "unknown",
        createdAt = createdAt ?: 0L
    )
    @Test
    fun testDTO() {
        // Test 1: valid DTO
        val valid = FollowNotificationDto("1", "userA", 123L).toModel()
        println("Valid DTO: ${valid.id == "1"} | ${valid.followerId == "userA"} | ${valid.createdAt == 123L}")
        // Test 2: null DTO
        val nullDto = FollowNotificationDto(null, null, null).toModel()
        println("Null DTO:   ${nullDto.id == ""} | ${nullDto.followerId == "unknown"} | ${nullDto.createdAt == 0L}")
    }
    enum class UserRole { ADMIN, USER, GUEST }
    fun String.toUserRole(): UserRole = when (this.lowercase()) {
        "admin" -> UserRole.ADMIN
        "user"  -> UserRole.USER
        else    -> UserRole.GUEST
    }
    data class UserDto(val id: String, val name: String, val role: String)
    data class User(val id: String, val name: String, val role: UserRole)
    fun UserDto.toModel(): User = User(
        id   = id,
        name = name,
        role = role.toUserRole()
    )
    // Bọc trong Result<T> — nếu mapping hoặc API lỗi → Failure tự động
    fun getUser(dto: UserDto): Result<User> = runCatching {
        dto.toModel()
    }
    @Test
    fun main() {
        val dto = UserDto("1", "Alice", "admin")
        val result = getUser(dto)
        // Dùng fold — xử lý cả 2 nhánh trong 1 biểu thức
        result.fold(
            onSuccess = { println(it) },        // User(id=1, name=Alice, role=ADMIN)
            onFailure = { println("Lỗi: $it") }
        )
        // Hoặc dùng getOrNull
        val user = result.getOrNull()
        println(user)  // User(id=1, name=Alice, role=ADMIN)
    }


}
