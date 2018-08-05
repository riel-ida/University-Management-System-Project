package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class VersionMonitorTest extends VersionMonitor {
	VersionMonitor vm;
	@Before
	public void setUp() {
		this.vm = new VersionMonitor();
	}

	@After
	public void tearDown(){
		this.vm = null;
	}

	@Test
	public void testGetVersion() {
		try{
			int x = vm.getVersion();
			try{
				assertTrue(x>=0);
			}
			catch(AssertionError ex){
				Assert.fail();
			}
		}
		catch(IllegalStateException ex){
			Assert.fail();
		}
		
		
	}

	@Test
	public void testInc() {
		try{
			int x = vm.getVersion();
			vm.inc();
			try{
				assertEquals(x+1, vm.getVersion());
			}
			catch(AssertionError  ex){
				Assert.fail();
			}
		}
		catch(Exception ex){
			Assert.fail();
		}
	}

	@Test
	public void testAwait() {
		Inc c = new Inc(vm);
		Thread t1 = new Thread(c);
		try{
				vm.await(vm.getVersion());
			try{
				t1.start();
				Thread.currentThread().join();
				}
			catch(InterruptedException ex){
				Assert.fail();
			}
		}
		catch(Exception ex){
			Assert.fail();
		}
	}
		
	class Inc implements Runnable {
	
		   private VersionMonitor vm;
		 
		   public Inc(VersionMonitor vm) {
		      this.vm = vm;
		   }
		 
		    public void run(){
		    	vm.inc();
		    }

	}
}