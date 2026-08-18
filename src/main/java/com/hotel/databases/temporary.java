package com.hotel.databases;

public class temporary
{
    public static void main(String[] args)
    {
        CardRoom room102 = new CardRoom("301", "SUITE", 25000);
        Result x = room102.updateRoomCard();
        System.out.println(x.message());
    }
}
//            case "STANDARD" -> 10000;
//                case "LUXE" -> 20000;
//                case "SUITE" -> 25000;