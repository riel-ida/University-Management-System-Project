package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PromiseTest<T> extends Promise<T> {
	Promise<Integer> p;
	@Before
	public void setUp()  {
		this.p = new Promise<Integer>();
	}

	@After
	public void tearDown()  {
		this.p = null;
	}

	@Test
	public void testGet() {
		try{
			p.get();
			Assert.fail();
			
		}
		catch(IllegalStateException ex){
			p.resolve(1);
		}
		try{
			int x = p.get();
			try{
				assertEquals(1, x);
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
	public void testIsResolved() {
		try{
		assertTrue(p.get()!=0);
		}
		catch(AssertionError ex){
			Assert.fail();
		}
	}

	@Test
	public void testResolve() {
		try{
			p.resolve(1);
			try{
				p.resolve(2);
				Assert.fail();
			}
			catch(IllegalStateException ex){
				int x = p.get();
				assertEquals(x,5);
			}
			catch(Exception ex){
			Assert.fail();
			}
		}
		catch(Exception ex){
			Assert.fail();
		}
	}

	@Test
	public void testSubscribe() {
			callback cb = new callback(){
				public void call(){
				}
			};
			

			try{
				p.subscribe(cb);
				try{
					p.subscribe(cb);
					Assert.fail();
				}
				catch(Exception ex){
					p.resolve(1);
				}
				try{
					p.subscribe(cb);
				}
				catch(Exception ex){
					Assert.fail();
				}
			}
			catch(Exception Ex){
				Assert.fail();
			}

	}
}
