package com.dbfconsult.bigint.clint;

import java.math.BigInteger;

import junit.framework.Assert;


import org.junit.Test;

import com.dbfconsult.bigint.clint.Clint;
import com.dbfconsult.opencl.NumberUtil;


public class ClintTest {
	@Test
	public void testClint() {
		for (int i = 0; i < 50; i++) {
			BigInteger bigInteger = NumberUtil.generateRandomBigInteger(100,
					300);
			Clint clint = new Clint(bigInteger);
			Assert.assertEquals(bigInteger, clint.asBigInteger());
		}
	}

	@Test
	public void testClint1a() {
		for (int i = 0; i < 50; i++) {
			BigInteger bigInteger = NumberUtil.generateRandomBigInteger(256,
					500);
			Clint clint = new Clint(bigInteger);
			Assert.assertEquals(bigInteger, clint.asBigInteger());
		}
	}

	@Test
	public void testClint2() {
		BigInteger bigInteger  = new BigInteger("4742888514018637507510929191370641264551755191181788702401803618957635430528071875925215246580330505876508554748661359489751730986027933690512775734261");
		Clint clint = new Clint(bigInteger);
		Assert.assertEquals(bigInteger, clint.asBigInteger());
		
		bigInteger  = new BigInteger("7327890534435987387248220321590925249137232261705039122059420437350927975173435670816760798885144525103336392620726612714990191708332829764767565515220753637033970");
		clint = new Clint(bigInteger);
		Assert.assertEquals(bigInteger, clint.asBigInteger());
		
		
	}
	
	
	
}
