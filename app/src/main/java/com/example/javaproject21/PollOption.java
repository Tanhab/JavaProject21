package com.example.javaproject21;

/**
 * The class for Poll option.
 */
public class PollOption {
    /**
     * The String for Poll name.
     */
    String pollName;
    /**
     * The String for Option id.
     */
    String optionId;
    /**
     * The String for Poll id.
     */
    String pollId;
    /**
     * The int variable for Count.
     */
    int count;

    /**
     * Instantiates a new Poll option.
     *
     * @param pollName the poll name
     * @param optionId the option id
     * @param pollId   the poll id
     * @param count    the count
     */
    public PollOption(String pollName, String optionId, String pollId, int count) {
        this.pollName = pollName;
        this.optionId = optionId;
        this.pollId = pollId;
        this.count = count;
    }

    /**
     * Instantiates a new Poll option.
     */
    public PollOption() {
    }

    /**
     * Instantiates a new Poll option.
     *
     * @param pollName the poll name
     * @param optionId the option id
     * @param count    the count
     */
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

    /**
     * Gets option id.
     *
     * @return the option id
     */
    public String getOptionId() {
        return optionId;
    }

    /**
     * Gets poll id.
     *
     * @return the poll id
     */
    public String getPollId() {
        return pollId;
    }

    /**
     * Sets poll id.
     *
     * @param pollId the poll id
     */
    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    /**
     * Sets option id.
     *
     * @param optionId the option id
     */
    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets poll name.
     *
     * @return the poll name
     */
    public String getPollName() {
        return pollName;
    }

    /**
     * Sets poll name.
     *
     * @param pollName the poll name
     */
    public void setPollName(String pollName) {
        this.pollName = pollName;
    }
}
