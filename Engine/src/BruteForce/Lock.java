package BruteForce;

public class Lock {
    private boolean lock;
    public Lock(boolean lock){
        this.lock = lock;
    }
    public void setLock(boolean value){ this.lock = value; }
    public boolean getLock(){ return lock; }

}
