public class Room
{
    private int roomNum;
    private Integer occupiedBy;
    private String roomType;

    public Room(int newRoomNum,
                Integer newOccupiedBy,
                String newRoomType)
    {
        roomNum = newRoomNum;
        occupiedBy = newOccupiedBy;
        roomType = newRoomType;
    }

    // Accessor methods
    public int getRoomNum()
    {
        return roomNum;
    }
    public Integer getOccupiedBy()
    {
        return occupiedBy;
    }
    public String getRoomType()
    {
        return roomType;
    }
    // Mutator methods
    public void setRoomNum(int newRoomNum)
    {
        roomNum = newRoomNum;
    }
    public void setOccupied(Integer newOccupiedBy)
    {
        occupiedBy = newOccupiedBy;
    }
    public void setRoomType(String newRoomType)
    {
        roomType = newRoomType;
    }
    // Methods to check if fields are formatted correctly and valid
    public static boolean checkRoomNum(int roomNumCheck)
    {
        return (roomNumCheck >= 1);
    }
    public static boolean checkRoomType(String roomTypeCheck)
    {
        return (roomTypeCheck.equals("single")
                || roomTypeCheck.equals("double")
                || roomTypeCheck.equals("twin")
                || roomTypeCheck.equals("quad"));
    }
}