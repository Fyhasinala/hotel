package com.hotel.databases;

public record Result (boolean status, String message)
{
    public static Result success(String message)
    {
        return new Result (true, message);
    }

    public static Result failure(String message)
    {
        return new Result (false, message);
    }

    public static Result exception()
    {
        return new Result (false, "If this error persist, please report to the dev");
    }
}
