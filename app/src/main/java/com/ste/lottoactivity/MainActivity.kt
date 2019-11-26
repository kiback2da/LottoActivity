package com.ste.lottoactivity

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    var lottoTxtList = ArrayList<TextView>() //구매번호 TextView
    var lottoNumList = ArrayList<Int>() // 구매번호 Int

    var lottoMyTxtList = ArrayList<TextView>() //내가 구매한 번호 TextView

    var lottoMyAutoNumList = ArrayList<Int>() //내가 자동으로 구매한 번호
    var lottoMyAutoTxtList = ArrayList<TextView>() //내가 자동으로 구매한 번호 TextView

    var bonusNum = 0

    var usedMoney = 0
    var winMoney = 0

    var firstCnt = 0
    var secondCnt = 0
    var thirdCnt = 0
    var fourthCnt = 0
    var fifthCnt = 0
    var failCnt = 0

    var autoPrice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpEvents()
        setValues()
    }
    override fun setUpEvents() {
        lottoBtnBuy.setOnClickListener {
            buyLotto()
            calculateMoney()
        }
        lottoBtnBuyAuto.setOnClickListener {
            buyLottoAuto()
        }
        lottoBtnMyNumBuyAuto.setOnClickListener {
            buyMyLottoAuto()
        }

        lottoBtnReset.setOnClickListener {
            finish()
            startActivity(getIntent())
        }
    }

    override fun setValues() {
        lottoTxtList.add(lottoTxtNum1)
        lottoTxtList.add(lottoTxtNum2)
        lottoTxtList.add(lottoTxtNum3)
        lottoTxtList.add(lottoTxtNum4)
        lottoTxtList.add(lottoTxtNum5)
        lottoTxtList.add(lottoTxtNum6)

        lottoMyTxtList.add(lottoEdtMyNum1)
        lottoMyTxtList.add(lottoEdtMyNum2)
        lottoMyTxtList.add(lottoEdtMyNum3)
        lottoMyTxtList.add(lottoEdtMyNum4)
        lottoMyTxtList.add(lottoEdtMyNum5)
        lottoMyTxtList.add(lottoEdtMyNum6)

        lottoMyAutoTxtList.add(lottoEdtMyNum1)
        lottoMyAutoTxtList.add(lottoEdtMyNum2)
        lottoMyAutoTxtList.add(lottoEdtMyNum3)
        lottoMyAutoTxtList.add(lottoEdtMyNum4)
        lottoMyAutoTxtList.add(lottoEdtMyNum5)
        lottoMyAutoTxtList.add(lottoEdtMyNum6)
    }

    fun buyLotto(){
        lottoNumList.clear()

        for(lottoTxt in lottoTxtList){ //6
            while(true) { //
                var randomNum = (Math.random()*45+1).toInt()
                var isDup = false

                Log.d("로그","${randomNum}")
                for (lottoNum in lottoNumList) {
                    if (lottoNum == randomNum) {
                        isDup = true
                    }
                }
                if (!isDup) {
                    lottoNumList.add(randomNum)
                    break
                }
            }
        }

        Collections.sort(lottoNumList)
        for(i in 0..lottoNumList.size-1){
            lottoTxtList[i].text = lottoNumList[i].toString()
        }

        while(true) {
            bonusNum = (Math.random()*45+1).toInt()
            var isDupBonus = false

            for (lottoNum in lottoNumList) {
                if (lottoNum == bonusNum) {
                    isDupBonus = true
                }
            }
            if (!isDupBonus) {
                lottoTxtBonusNum.text = bonusNum.toString()
                break
            }
        }
    }

    fun calculateMoney(){
        var correctCnt = 0
        for(i in 0..lottoNumList.size-1){
            if(lottoNumList[i] == lottoMyTxtList[i].text.toString().toInt()){
                correctCnt++
            }
        }

        when(correctCnt){
            6 -> { winMoney += 2000000000
                firstCnt++
            }
            5 -> {
                if(lottoNumList.indexOf(bonusNum) == 1){
                    winMoney += 50000000
                    secondCnt++
                }else{
                    winMoney += 1500000
                    thirdCnt++
                }
            }
            4 -> { winMoney += 50000
                fourthCnt++
            }
            3 -> { usedMoney -= 5000
                fifthCnt++
            }
            else -> {
                failCnt++
            }
        }

        usedMoney += 1000
        lottoTxtUsedMoney.text = String.format("사용 금액 : %,d원", usedMoney)
        lottoTxtWinMoney.text = String.format("당첨 금액 : %,d원", winMoney)

        lottoTxtWinFirstCnt.text = String.format("1등 : %,d", firstCnt)
        lottoTxtWinSecondCnt.text = String.format("2등 : %,d", secondCnt)
        lottoTxtWinThirdCnt.text = String.format("3등 : %,d", thirdCnt)
        lottoTxtWinFourthCnt.text = String.format("4등 : %,d", fourthCnt)
        lottoTxtWinFifthCnt.text = String.format("5등 : %,d", fifthCnt)
        lottoTxtWinFailCnt.text = String.format("꽝 : %,d", failCnt)
    }

    var run = object : Runnable{
        override fun run() {

            if(lottoEdtAutoPrice.text.toString() == "" ){
                autoPrice = 100000000
            }else{
                autoPrice = lottoEdtAutoPrice.text.toString().toInt()
            }

            if(usedMoney <= autoPrice){
                buyLotto()
                calculateMoney()
                buyLottoAuto()
            }else{
                buyLottoAutoStop()
            }
        }
    }

    fun buyLottoAuto(){
        Handler().post(run)
    }

    fun buyLottoAutoStop(){
        Handler().removeCallbacks(run)
    }

    fun buyMyLottoAuto(){
        lottoMyAutoNumList.clear()

        for(lottoTxt in lottoMyAutoTxtList){ //6
            while(true) { //
                var randomNum = (Math.random()*45+1).toInt()
                var isDup = false

                for (lottoNum in lottoMyAutoNumList) {
                    if (lottoNum == randomNum) {
                        isDup = true
                    }
                }
                if (!isDup) {
                    lottoMyAutoNumList.add(randomNum)
                    break
                }
            }
        }

        Collections.sort(lottoMyAutoNumList)
        for(i in 0..lottoMyAutoNumList.size-1){
            lottoMyAutoTxtList[i].text = lottoMyAutoNumList[i].toString()
        }
    }
}
