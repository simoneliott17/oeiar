public class Date {
    private int year;
    private int month;
    private int day;
    private int secondsPassed=0;
    final private int SECONDSINDAY = 86400;
    final private int[] DAYSINMONTH = {31,28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    public Date(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void updateTime(double seconds){
        secondsPassed+=seconds;
        if(secondsPassed>SECONDSINDAY){
            day+=secondsPassed/SECONDSINDAY;
            secondsPassed %= SECONDSINDAY;
        }
        int daysThisMonth = DAYSINMONTH[month-1];
        if(day>daysThisMonth){
            month+=day/daysThisMonth;
            //System.out.println("jdfjfjf"+day/daysThisMonth);
            day%=daysThisMonth;

        }
        if(month>DAYSINMONTH.length){
            year+=month/DAYSINMONTH.length;
            month%=DAYSINMONTH.length;
        }
    }

    public String getDate(){
        return(String.format("%d/%d/%d", day, month, year));
    }
}