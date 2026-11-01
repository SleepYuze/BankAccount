public class Main
{
    static void main(String[] args)
    {
        System.out.println("=== Демонстрация BankAccount + Balance ===\n");

        BankAccount rubAccount = new BankAccount("Иван Петров", Currency.RUB, 5000000L);
        BankAccount usdAccount = new BankAccount("John Smith", Currency.USD, 100000L);
        BankAccount eurAccount = new BankAccount("Anna Müller", Currency.EUR, 50000L);

        System.out.println("Созданные счета:");
        System.out.println("  " + rubAccount);
        System.out.println("  " + usdAccount);
        System.out.println("  " + eurAccount);
        System.out.println();

        rubAccount.deposit(10000000L);
        usdAccount.deposit(20000L);
        System.out.println("После пополнения:");
        System.out.println("  " + rubAccount);
        System.out.println("  " + usdAccount);
        System.out.println();

        rubAccount.withdraw(1000000L);
        System.out.println("После снятия 10 000 ₽:");
        System.out.println("  " + rubAccount);
        System.out.println();

        rubAccount.transfer(usdAccount, 4800000L);
        System.out.println("После перевода 48 000 ₽ -> USD:");
        System.out.println("  " + rubAccount);
        System.out.println("  " + usdAccount);
        System.out.println();

        eurAccount.deposit(100000L);
        eurAccount.transfer(rubAccount, 10000L);
        System.out.println("После перевода 100 € -> RUB:");
        System.out.println("  " + eurAccount);
        System.out.println("  " + rubAccount);
        System.out.println();

        Balance rubBalance = new Balance(1000000L, Currency.RUB);
        System.out.println("Конвертация:");
        System.out.println("  10 000 ₽ -> " + rubBalance.convTo(Currency.USD));
        System.out.println("  10 000 ₽ -> " + rubBalance.convTo(Currency.EUR));
        System.out.println("  10 000 ₽ -> " + rubBalance.convTo(Currency.CNY));
        System.out.println();

        System.out.println("=== Корнер-кейсы ===");
        tryCorner("Отрицательное пополнение", () -> rubAccount.deposit(-100));
        tryCorner("Превышение лимита", () -> rubAccount.withdraw(99999900L));
        tryCorner("Перевод самому себе", () -> rubAccount.transfer(rubAccount, 100));
        tryCorner("Отрицательный баланс", () -> new Balance(-1, Currency.RUB));
        tryCorner("Null валюта", () -> new Balance(100, null));

        usdAccount.block();
        tryCorner("Операция на заблокированном счёте", () -> usdAccount.deposit(100));
        usdAccount.unlock();
        System.out.println("\nСчёт разблокирован: " + usdAccount);

        System.out.println("\n=== Иммутабельность Balance ===");
        Balance original = new Balance(50000L, Currency.RUB);
        Balance updated = original.add(10000L);
        System.out.println("  original: " + original);
        System.out.println("  updated:  " + updated);
        System.out.println("  original не изменился: " + (original.getAmt() == 50000L));
    }

    private static void tryCorner(String label, Runnable action)
    {
        try
        {
            action.run();
            System.out.println("  [OK]     " + label);
        }
        catch (RuntimeException e)
        {
            System.out.println("  [ОШИБКА] " + label + " -> " + e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
    }
}
