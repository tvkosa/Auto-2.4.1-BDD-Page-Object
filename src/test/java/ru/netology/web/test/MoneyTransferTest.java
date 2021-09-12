package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.ReplenishCards;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    @BeforeEach
    public void setUpAll() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferMoneyFromSecondCardToFirstCard() {
        var cardsInfo = DataHelper.getCardsInfo();
        var cards = new DashboardPage();
        int firstBalanceBefore = cards.getFirstCardBalance();
        int secondBalanceBefore = cards.getSecondCardBalance();
        int replenishSum = 100;
        var replenishThisCard = cards.replenishFirst();
        replenishThisCard.replenish(Integer.toString(replenishSum), cardsInfo, 1);
        assertEquals(firstBalanceBefore + replenishSum, cards.getFirstCardBalance());
        assertEquals(secondBalanceBefore - replenishSum, cards.getSecondCardBalance());
    }

    @Test
    public void shouldTransferMoneyFromFirstCardToSecondCard() {
        var cardsInfo = DataHelper.getCardsInfo();
        var cards = new DashboardPage();
        int firstBalanceBefore = cards.getFirstCardBalance();
        int secondBalanceBefore = cards.getSecondCardBalance();
        int replenishSum = 100;
        var replenishThisCard = cards.replenishSecond();
        replenishThisCard.replenish(Integer.toString(replenishSum), cardsInfo, 2);
        assertEquals(firstBalanceBefore - replenishSum, cards.getFirstCardBalance());
        assertEquals(secondBalanceBefore + replenishSum, cards.getSecondCardBalance());
    }
    @Test
    public void shouldGetErrorIfAmountGreaterBalance() {
        var cardsInfo = DataHelper.getCardsInfo();
        var cards = new DashboardPage();
        var replenish = new ReplenishCards();
        int firstBalanceBefore = cards.getFirstCardBalance();
        int secondBalanceBefore = cards.getSecondCardBalance();
        int replenishSum = 70000;
        var replenishCard = cards.replenishSecond();
        replenishCard.replenish(Integer.toString(replenishSum), cardsInfo, 2);
        replenish.errorIfTransferIsMoreThenBalance();
        assertEquals(firstBalanceBefore, cards.getFirstCardBalance());
        assertEquals(secondBalanceBefore, cards.getSecondCardBalance());
    }
}
