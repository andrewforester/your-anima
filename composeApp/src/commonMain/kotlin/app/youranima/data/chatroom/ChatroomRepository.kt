package app.youranima.data.chatroom

/** Source of the Chatroom tab data. [MockChatroomRepository] until there is a backend. */
interface ChatroomRepository {
    fun chatroomData(): ChatroomData
}
