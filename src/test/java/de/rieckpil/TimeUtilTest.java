package de.rieckpil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TimeUtilTest {

  @Mock
  private TimeProvider timeProvider;

  @InjectMocks
  private TimeUtil cut;

  @Test
  void shouldThrowExceptionWhenDateIsInFuture() throws Exception {


    when(timeProvider.getCurrentDate())
      .thenReturn(LocalDate.of(2020, 1, 1));

    LocalDate creationDate = LocalDate.now().plusDays(1);

    assertThrows(IllegalArgumentException.class, () -> {
      cut.getDiffBetweenCreationDate(creationDate);
    });
  }


  @Test
  void shouldReturnTodayWhenCommentWasCreatedToday() throws Exception {
    when(timeProvider.getCurrentDate())
      .thenReturn(LocalDate.now());

    LocalDate creationDate = LocalDate.now();

    String result = cut.getDiffBetweenCreationDate(creationDate);

    assertEquals("today", result);
  }

  @Test
  void shouldReturnMoreThanYearWhenCommentWasCratedLastYear() throws Exception {
    when(timeProvider.getCurrentDate())
      .thenReturn(LocalDate.now());

    LocalDate lastMonth = LocalDate.now().minusDays(370);

    String result = cut.getDiffBetweenCreationDate(lastMonth);

    assertEquals("more than a year ago", result);

  }

  @Test
  void shouldReturnOneMonthAgoWhenCommentWasCreatedLastMonth() throws Exception {
    when(timeProvider.getCurrentDate())
      .thenReturn(LocalDate.now());

    LocalDate lastMonth = LocalDate.now().minusDays(40);

    String result = cut.getDiffBetweenCreationDate(lastMonth);

    assertEquals("one month ago", result);
  }

  @Test
  void shouldReturnPluralOfMonthsWhenCommentIsCreatedMoreThanAMonthAgo() throws Exception {
    when(timeProvider.getCurrentDate())
      .thenReturn(LocalDate.now());

    LocalDate lastMonth = LocalDate.now().minusDays(70);

    String result = cut.getDiffBetweenCreationDate(lastMonth);

    assertEquals("2 months ago", result);
  }

  @Test
  void shouldReturnOneDayAgoWhenCommentWasMadeYesterday() throws Exception {
    when(timeProvider.getCurrentDate())
      .thenReturn(LocalDate.now());

    LocalDate yesterday = LocalDate.now().minusDays(1);

    String result = cut.getDiffBetweenCreationDate(yesterday);

    assertEquals("one day ago", result);
  }

}
