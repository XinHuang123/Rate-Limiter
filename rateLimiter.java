package rateLimiter;
/*
 * 最近遇到一个场景，在每分钟错误计数达到250时发送消息。这里的每分钟并不是说整点的几分，有可能是现在16：16：16到16：17：16。
 * Token Bucket
 * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/TimeUnit.html
 * */
import java.util.concurrent.TimeUnit;

public class rateLimiter {

	private final int capacity; //total number of tokens the system allowed
	private final int tokenPerSecond; //incoming token rate
	private int tokens; //tokens available
	private long timeStamp = System.currentTimeMillis(); //store the previous request time
	
	public rateLimiter(int tokenPerUnit, TimeUnit unit){
		tokenPerSecond = (int)(tokenPerUnit/unit.toSeconds(1L));
		capacity = (int)(tokenPerUnit/unit.toSeconds(1L));
	}
	
	public boolean take(){
		long now = System.currentTimeMillis();
		tokens+= tokenPerSecond*(now-timeStamp)/1000;
		if(tokens>=capacity) tokens = capacity;
		if(tokens<1) return false;
		else {
			tokens--;
			timeStamp = now;
			return true;
		}
	}
	
	public static void main(String[]args) throws InterruptedException{		
		rateLimiter bucket = new rateLimiter(6,TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(1L);
		for(int i=0;i<=6;i++) System.out.println(bucket.take());
		TimeUnit.SECONDS.sleep(1L);
		for(int i=0;i<=6;i++) System.out.println(bucket.take());
	}
	
}
