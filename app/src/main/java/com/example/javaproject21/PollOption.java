package com.example.javaproject21;

public class PollOption {
    String pollName,optionId,pollId;
    int count;

    public PollOption(String pollName, String optionId, String pollId, int count) {
        this.pollName = pollName;
        this.optionId = optionId;
        this.pollId = pollId;
        this.count = count;
    }

    public PollOption() {
    }

    public PollOption(String pollName, String optionId, int count) {
        this.pollName = pollName;
        this.optionId = optionId;
        this.count = count;
    }

    @Override
    public String toString() {
        return "PollOption{" +
                "pollName='" + pollName + '\'' +
                ", optionId='" + optionId + '\'' +
                ", count=" + count +
                '}';
    }

    public String getOptionId() {
        return optionId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }
}
