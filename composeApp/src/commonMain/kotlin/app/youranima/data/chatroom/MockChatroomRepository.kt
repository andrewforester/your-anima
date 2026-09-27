package app.youranima.data.chatroom

/** Static data from the original app's screenshot plus two invented chats (SPEC chatroom → Data). */
object MockChatroomRepository : ChatroomRepository {
    override fun chatroomData() =
        ChatroomData(
            promo =
                PsychicsPromo(
                    psychicsCount = 900,
                    rating = 4.4,
                    reviewCount = 1_000_324,
                    avatars = listOf(ChatAvatar.Luna, ChatAvatar.WhimsyLou, ChatAvatar.PromoFront),
                ),
            chats =
                listOf(
                    chat(
                        "chandra",
                        "Chandra",
                        ChatAvatar.Chandra,
                        "02:54 pm",
                        "hey, you're online and so am I. want a quick read on why this keeps pulling you back? " +
                            "I'm free right now.",
                        canCall = true,
                    ),
                    chat(
                        "esther_eclipse",
                        "Esther Eclipse",
                        ChatAvatar.EstherEclipse,
                        "Sept 25, 01:13 pm",
                        "hi, I came across your profile and caught this sense that you've been carrying something " +
                            "heavy lately. want to talk about it?",
                    ),
                    chat(
                        "ana",
                        "Ana",
                        ChatAvatar.Ana,
                        "Sept 24, 05:40 pm",
                        "Hi. I saw you join and I'm getting a clear \"boundary test\" vibe. Not necessarily a bad " +
                            "thing, but someone is checking how far they can go. Want me to look closer?",
                    ),
                    chat(
                        "miss_sheyna",
                        "Miss Sheyna",
                        ChatAvatar.MissSheyna,
                        "Sept 24, 12:40 pm",
                        "hi, I saw you pop online and I pulled the Eight of Wands. this usually shows news arriving " +
                            "fast. curious where it's coming from?",
                    ),
                    chat(
                        "madam_sarah",
                        "Madam Sarah",
                        ChatAvatar.MadamSarah,
                        "Sept 23, 06:11 pm",
                        "Hi. This feels like a situation where you're doing everything right and it's still not " +
                            "moving. Let's see what's blocking it.",
                    ),
                    chat(
                        "lyrienne",
                        "Lyrienne",
                        ChatAvatar.Lyrienne,
                        "Sept 23, 06:00 pm",
                        "Hello. One quick pull. Page of Swords. This gives \"watching, checking, rereading messages\" " +
                            "energy. Is someone on your mind?",
                    ),
                    chat(
                        "mystic_orion",
                        "Mystic Orion",
                        null,
                        "Sept 22, 09:15 pm",
                        "Thanks for the chat today. Remember: the Moon in your sign makes this week great for new starts.",
                        online = true,
                        canCall = true,
                        unread = 0,
                    ),
                    chat(
                        "luna",
                        "Luna",
                        ChatAvatar.Luna,
                        "Sept 20, 11:02 am",
                        "Your cards are ready whenever you are. Come back and we'll look at them together.",
                        unread = 0,
                    ),
                ),
        )

    private fun chat(
        id: String,
        name: String,
        avatar: ChatAvatar?,
        time: String,
        message: String,
        online: Boolean = false,
        canCall: Boolean = false,
        unread: Int = 1,
    ) = ChatPreview(
        id = id,
        psychicName = name,
        avatar = avatar,
        online = online,
        canCall = canCall,
        timeLabel = time,
        lastMessage = message,
        unreadCount = unread,
    )
}
