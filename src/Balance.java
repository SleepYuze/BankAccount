import java.util.Objects;

public final  class Balance
{

    private final long amtKop;
    private final Currency curr;
    public Balance(long amtKop, Currency curr)
    {
        if(amtKop < 0)
        {
            throw new IllegalArgumentException("Баланс не может быть отрицательным: " + amtKop);
        }
        this.amtKop=amtKop;
        this.curr=Objects.requireNonNull(curr, "Валюта не может быть пустой");
    }
    public static Balance zero(Currency curr)
    {
        return new Balance(0L,curr);
    }
    public Currency getCurrency()
    {
        return curr;
    }
    public long getAmt()
    {
        return amtKop;
    }
    public Balance add(long kop)
    {
        if(kop < 0)
        {
            throw new IllegalArgumentException("Нельзя прибавить отрицательную сумму:" + kop);
        }
        return new Balance(amtKop+kop, this.curr);
    }
    public  Balance add(Balance other)
    {
        Objects.requireNonNull(other, "Другой баланс не должен быть null");
        if(this.curr!=other.curr)
        {
            throw new IllegalArgumentException("Нельзя складывать разные валюты" + other.curr);
        }
        return add(this.amtKop+other.amtKop);
    }
    public Balance sub(long kop)
    {
        if(kop < 0)
        {
            throw new IllegalArgumentException("Нельзя прибавить отрицательную сумму" + kop);
        }
        return new Balance(this.amtKop-kop,this.curr);
    }
    public Balance sub(Balance other)
    {
        Objects.requireNonNull(other, "Баланс не должен быть null");
        if(this.curr!=other.curr)
        {
            throw new IllegalArgumentException("Нельзя складывать разные валюты" + other.curr);
        }
        return sub(other.amtKop);
    }
    public Balance convTo(Currency curr)
    {
        Objects.requireNonNull(curr,"Валюта не должна быть null");
        long valRubKop = this.amtKop * this.curr.getPriceInRublesKopecks();
        long converted = valRubKop / curr.getPriceInRublesKopecks();
        return new Balance(converted,curr);
    }
    public boolean compare(Balance other)
    {
        Objects.requireNonNull(other, "other не может быть null");
        if (this.curr != other.curr)
        {
            throw new IllegalArgumentException("Разные валюты");
        }
        return this.amtKop >= other.amtKop;
    }
}

