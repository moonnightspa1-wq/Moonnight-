package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SpaDataDefaults
import com.example.model.SpaDuration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Moonnight", appName)
  }

  @Test
  fun `verify spa services and pricing matrix`() {
    assertEquals(9, SpaDataDefaults.services.size)

    val swedish = SpaDataDefaults.services.first { it.id == "swedish" }
    assertEquals(1800, swedish.getPrice(SpaDuration.MIN_30))
    assertEquals(2800, swedish.getPrice(SpaDuration.MIN_60))
    assertEquals(3800, swedish.getPrice(SpaDuration.MIN_90))

    val deepTissue = SpaDataDefaults.services.first { it.id == "deep_tissue" }
    assertEquals(2000, deepTissue.getPrice(SpaDuration.MIN_30))
    assertEquals(3200, deepTissue.getPrice(SpaDuration.MIN_60))
    assertEquals(4200, deepTissue.getPrice(SpaDuration.MIN_90))
  }
}
