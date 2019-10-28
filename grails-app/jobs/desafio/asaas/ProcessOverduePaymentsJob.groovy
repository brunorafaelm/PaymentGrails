package desafio.asaas

import com.asaas.challenge.PaymentService

class ProcessOverduePaymentsJob {

    static triggers = {
        cron name:   'oversuePayment',   startDelay: 10000, cronExpression: '0 0 8 ? * MON-FRI *'
    }

    def execute() {
        PaymentService service = new PaymentService()
        service.processOverduePayments()
    }
}
