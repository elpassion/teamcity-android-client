package pl.elpassion.eltc.details

import android.support.test.espresso.Espresso.pressBackUnconditionally
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.elpassion.android.commons.espresso.InitIntentsRule
import com.elpassion.android.commons.espresso.checkIntent
import com.elpassion.android.commons.espresso.isDisplayed
import com.elpassion.android.commons.espresso.onText
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.elpassion.eltc.*
import pl.elpassion.eltc.builds.BuildsActivity

@RunWith(AndroidJUnit4::class)
class DetailsActivityTest {

    @JvmField @Rule
    val activityRule = ActivityTestRule(DetailsActivity::class.java)

    @JvmField @Rule
    val intents = InitIntentsRule()

    val states: Subject<AppState> = BehaviorSubject.createDefault(InitialState)

    val model: TeamCityModel = mock { on { state } doReturn states }

    init {
        DI.provideTeamCityModel = { model }
    }

    @Test
    fun Display_build_number() {
        states.onNext(BuildDetailsState(createBuild(number = "76")))
        onText("#76").isDisplayed()
    }

    @Test
    fun Return_to_list_on_back_pressed() {
        states.onNext(BuildDetailsState(createBuild()))
        pressBackUnconditionally()
        verify(model).perform(argThat { this is ReturnToList })
    }

    @Test
    fun Display_builds_screen_on_loading_builds_list() {
        states.onNext(LoadingState)
        checkIntent(BuildsActivity::class.java)
        Assert.assertTrue(activityRule.activity.isFinishing)
    }
}