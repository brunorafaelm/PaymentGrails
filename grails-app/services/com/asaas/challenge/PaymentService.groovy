package com.asaas.challenge

import grails.gorm.transactions.Transactional

@Transactional
public class PaymentService {

	PaymentScopes paymentScopes = new PaymentScopes()

	public List<Payment> list(params) {
		return Payment.query(params).list(max: params.max, offset: params.offset)
	}

	public Payment save(params) {
		Payment payment = new Payment()

		payment.customerName = params.customerName
		payment.value = params.value
		payment.deleted = params.deleted
		payment.status = params.status
		payment.paymentForm = params.paymentForm
		payment.email = params.email
		payment.cpf = params.cpf
		payment.dueDate = params.dueDate
		payment.description = params.description

		return payment.save(failOnError: true);
	}

	public Payment update(Long id, params) {
		Payment payment = Payment.get(id)

		if(paymentScopes.isPaymentPaid(payment)){
			payment.errors.reject('Cobrança paga não pode ser alterada.')
			return payment
		}

		payment.id = id
		payment.customerName = params.customerName
		payment.value = params.value
		payment.deleted = params.deleted
		payment.status = params.status
		payment.paymentForm = params.paymentForm
		payment.email = params.email
		payment.cpf = params.cpf
		payment.dueDate = params.dueDate
		payment.description = params.description

		return payment.save(failOnError: true)
	}

	public Payment edit(Long id){
		Payment payment = Payment.get(id)

		if(paymentScopes.isPaymentPaid(payment)){
			payment.errors.reject('Cobrança paga não pode ser alterada ou deletada.')
		}

		return payment
	}

	public Payment delete(Long id) {
		Payment payment = Payment.get(id)

		if(payment.hasErrors()){
			return payment
        }

		if(paymentScopes.isPaymentPaid(payment)){
			payment.errors.reject('Cobrança paga não pode ser deletada.')
			return payment
		}

		return payment.delete()
	}

	public void processOverduePayments() {
		def params = null
		List<Payment> listaVencida = Payment.overduePayment(params).list()

		for(Payment item : listaVencida){
			item.status = PaymentStatus.OVERDUE
		}
	}
}
