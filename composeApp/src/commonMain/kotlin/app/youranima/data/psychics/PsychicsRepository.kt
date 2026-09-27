package app.youranima.data.psychics

/** Source of the Psychics tab data. [MockPsychicsRepository] until there is a backend. */
interface PsychicsRepository {
    fun psychicsData(): PsychicsData
}
