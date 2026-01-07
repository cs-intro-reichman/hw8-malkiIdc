/**
 * Represents a social network. The network has users, who follow other users.
 * Each user is an instance of the User class.
 * 
 * NOTE: This implementation assumes that the User class (which is not provided
 * here but is described in the HW document) has the following public methods:
 * - public String getName()
 * - public boolean addFollowee(String name)
 * - public boolean follows(String name)
 * - public int countMutual(User otherUser)
 * - public String toString()
 */
public class Network {

    // Fields
    private User[] users; // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /**
     * Creates a network with a given maximum number of users.
     */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /**
     * Creates a network with some users. The only purpose of this constructor is
     * to allow testing the toString and getUser methods, before implementing other methods.
     */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        // Assuming User objects can be created without being in the network yet
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }

    /**
     * Finds in this network, and returns, the user that has the given name.
     * If there is no such user, returns null.
     * Notice that the method receives a String, and returns a User object.
     */
    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            // Using equals() for String comparison as required
            if (users[i].getName().equals(name)) { 
                return users[i];
            }
        }
        return null;
    }

    /**
     * Adds a new user with the given name to this network.
     * If the network is full, does nothing and returns false;
     * If the given name is already a user in this network, does nothing and returns false;
     * Otherwise, creates a new user with the given name, adds the user to this network, and returns true.
     */
    public boolean addUser(String name) {
        // 1. Check if the network is full
        if (userCount == users.length) {
            return false;
        }

        // 2. Check if the given name is already a user
        if (getUser(name) != null) {
            return false;
        }

        // 3. Create a new user, add to the network, and increment count
        users[userCount] = new User(name);
        userCount++;
        return true;
    }

    /**
     * Makes the user with name1 follow the user with name2. If successful, returns true.
     * If any of the two names is not a user in this network,
     * or if the "follows" addition failed for some reason, returns false.
     */
    public boolean addFollowee(String name1, String name2) {
        // 1. Get user1 (the one who follows)
        User user1 = getUser(name1);
        if (user1 == null) {
            return false; // name1 is not a user
        }

        // 2. Get user2 (the one being followed). Check if name2 is a user in the network
        User user2 = getUser(name2);
        if (user2 == null) {
            return false; // name2 is not a user
        }

        // 3. Call the User class's addFollowee method
        // This method will handle internal failures like already following or follow-list full.
        return user1.addFollowee(name2);
    }

    /**
     * For the user with the given name, recommends another user to follow. The recommended user is
     * the user that has the maximal mutual number of followees as the user with the given name.
     */
    public String recommendWhoToFollow(String name) {
        User targetUser = getUser(name);
        if (targetUser == null) {
            return null;
        }

        int maxMutual = -1;
        String recommendedUserName = null;

        for (int i = 0; i < userCount; i++) {
            User potentialFollowee = users[i];

            // Skip the user on which the method was called
            if (potentialFollowee.getName().equals(name)) {
                continue;
            }

            // Calculate mutual followees, assuming countMutual is implemented in User class
            int currentMutual = targetUser.countMutual(potentialFollowee);

            if (currentMutual > maxMutual) {
                maxMutual = currentMutual;
                recommendedUserName = potentialFollowee.getName();
            }
        }

        return recommendedUserName;
    }

    /**
     * Computes and returns the name of the most popular user in this network:
     * The user who appears the most in the follow lists of all the users.
     */
    public String mostPopularUser() {
        int maxCount = -1;
        String mostPopularName = null;

        for (int i = 0; i < userCount; i++) {
            String currentUserName = users[i].getName();
            
            // Use the helper method to get the followee count for the current user
            int currentCount = followeeCount(currentUserName);

            if (currentCount > maxCount) {
                maxCount = currentCount;
                mostPopularName = currentUserName;
            }
        }

        return mostPopularName;
    }

    /**
     * Returns the number of times that the given name appears in the follows lists of all
     * the users in this network. Note: A name can appear 0 or 1 times in each list.
     */
    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            // Check if the current user (users[i]) follows the user with the given 'name'
            // This assumes the User class has a 'follows' method
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    // Returns a textual description of all the users in this network, and who they follow.
    public String toString() {
        // Must start with "Network:" to match test output
        String ans = "Network:\n"; 
        
        // Have each user print itself
        for (int i = 0; i < userCount; i++) {
            ans = ans + users[i].toString() + "\n";
        }
        return ans;
    }
}