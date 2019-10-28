package com.asaas.challenge

public class PaymentScopes{

public Boolean validateCpf(String cpf){
		if(cpf == null){
			return true
		}

		if (cpf == "00000000000" ||
				cpf == "11111111111" ||
				cpf == "22222222222" || cpf == "33333333333" ||
				cpf == "44444444444" || cpf == "55555555555" ||
				cpf == "66666666666" || cpf == "77777777777" ||
				cpf == "88888888888" || cpf == "99999999999" ||
				(cpf.length() != 11)){
			return false
		}

		char dig10, dig11
		int sm, i, r, num, peso

		try {
			sm = 0
			peso = 10
			for (i=0; i<9; i++) {
				num = (int)(cpf.charAt(i) - 48)
				sm = sm + (num * peso)
				peso = peso - 1
			}

			r = 11 - (sm % 11)
			if ((r == 10) || (r == 11))
				dig10 = '0'
			else dig10 = (char)(r + 48)

			sm = 0
			peso = 11
			for(i=0; i<10; i++) {
				num = (int)(cpf.charAt(i) - 48)
				sm = sm + (num * peso)
				peso = peso - 1
			}

			r = 11 - (sm % 11)
			if ((r == 10) || (r == 11))
				dig11 = '0'
			else dig11 = (char)(r + 48)

			if (!(dig10 == cpf.charAt(9)) && !(dig11 == cpf.charAt(10))) {	
                return false
			}else{
				return true
			}
		} catch (InputMismatchException erro) {	
            return erro
		}
	}

public Boolean isPaymentPaid(Payment payment){
		if(payment.status == PaymentStatus.PAID){
			return true
		}

		return false
	}	
}