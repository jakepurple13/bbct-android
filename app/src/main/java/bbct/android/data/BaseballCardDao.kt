package bbct.android.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BaseballCardDao {
    @Insert
    fun insertBaseballCard(card: BaseballCard)

    @Update
    fun updateBaseballCard(card: BaseballCard)

    @Delete
    fun deleteBaseballCards(cards: List<BaseballCard>)

    @get:Query("SELECT * FROM baseball_cards")
    val baseballCards: LiveData<List<BaseballCard>>

    @Query(
        "SELECT * FROM baseball_cards " +
        "WHERE brand LIKE :brand " +
        "  AND year = :year " +
        "  AND number LIKE :number " +
        "  AND player_name LIKE :playerName " +
        "  AND team LIKE :team"
    )
    fun getBaseballCards(
        brand: String? = null,
        year: Int? = null,
        number: String? = null,
        playerName: String? = null,
        team: String? = null
    ): LiveData<List<BaseballCard>>

    @Query("SELECT * FROM baseball_cards WHERE _id = :id")
    fun getBaseballCard(id: Long): BaseballCard?

    @get:Query("SELECT DISTINCT(brand) FROM baseball_cards")
    val brands: LiveData<List<String>>

    @get:Query("SELECT DISTINCT(player_name) FROM baseball_cards")
    val playerNames: LiveData<List<String>>

    @get:Query("SELECT DISTINCT(team) FROM baseball_cards")
    val teams: LiveData<List<String>>
}