package com.khfire22.af;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MainActivityTest {

    MainActivity mainActivity;


    @Before
    public void setUp() throws Exception {
        // Create instance of the mainActivity
        mainActivity = new MainActivity();
    }


    @Test
    public void testForMainActivityData() {

        //Test to see if there is a valid mainAct promotion
        Assert.assertTrue("MainAct Promotion Error", MainActivity.getTagPromotions() != null);

        //Test to see if there is a valid mainAct promo image
        Assert.assertTrue("MainAct Image Error", MainActivity.getTagPromoImage() != null);

        //Test to see if there is a valid mainAct promo title
        Assert.assertTrue("MainAct Title Error", MainActivity.getTagPromoTitle() != null);

        //Test to see if there is a valid mainAct promo description
        Assert.assertTrue("MainAct Description Error", MainActivity.getTagPromoDescription() != null);

        //Test to see if there is a valid mainAct promo footer
        Assert.assertTrue("MainAct Footer Error", MainActivity.getTagPromoFooter() != null);
    }


    @After
    public void tearDown() throws Exception {
    }




}

