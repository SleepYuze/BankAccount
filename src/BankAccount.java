import java.util.Objects;

public final class BankAccount {
    private final String name;
    private final Currency curr;
    private Balance bal;
    private long limit;
    private boolean blocked;

    public BankAccount(String name, Currency curr, long limit) {
        this.name = Objects.requireNonNull(name, "name не может быть null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name не может быть пустым");
        }
        this.curr = Objects.requireNonNull(curr, "currency не может быть null");
        if (limit < 0) {
            throw new IllegalArgumentException("limit не может быть отрицательным");
        }
        this.limit = limit;
        this.bal = Balance.zero(curr);
        this.blocked = false;
    }

    public String getName() {
        return name;
    }

    public Currency getCurr() {
        return curr;
    }

    public Balance getBal() {
        return bal;
    }

    public long getLimit() {
        return limit;
    }

    public boolean getBlocked() {
        return blocked;
    }

    public void setLimit(long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit не может быть меньше нуля" + limit);
        }
        this.limit = limit;
    }

    public void block() {
        this.blocked = true;
    }

    public void unlock() {
        this.blocked = false;
    }

    public void deposit(long kop) {
        ensureActive();
        if (kop < 0) {
            throw new IllegalArgumentException("Депозит больше нуля" + kop);
        }
        this.bal = bal.add(kop);
    }

    public void withdraw(long kop) {
        ensureActive();
        if (kop <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть положительной");
        }
        if (kop > limit) {
            throw new IllegalStateException(
                    "Превышен лимит на одну операцию: " + kop + " > " + limit);
        }
        this.bal = bal.sub(kop);
    }

    public void transfer(BankAccount target, long kop) {
        Objects.requireNonNull(target, "target не может быть null");
        if (target == this) {
            throw new IllegalArgumentException("Нельзя переводить самому себе");
        }
        ensureActive();
        target.ensureActive();

        if (kop <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }
        if (kop > limit) {
            throw new IllegalStateException("Превышен лимит перевода: " + kop + " > " + limit);
        }

        this.bal = bal.sub(kop);

        long credited;
        if (this.curr == target.curr) {
            credited = kop;
        } else {
            Balance sourcePart = new Balance(kop, this.curr);
            Balance converted = sourcePart.convTo(target.curr);
            credited = converted.getAmt();
        }

        target.bal = target.bal.add(credited);
    }

    private void ensureActive() {
        if (blocked) {
            throw new IllegalStateException("Счёт заблокирован: " + name);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "BankAccount{owner='%s', currency=%s, balance=%s, limit=%d коп., blocked=%s}",
                name, curr, bal, limit, blocked);
    }
}
