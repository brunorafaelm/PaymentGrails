package com.asaas.challenge

class Payment {

	String customerName
	
	BigDecimal value
	
	Boolean deleted = false
	
	PaymentStatus status = PaymentStatus.PENDING
	
	PaymentForm paymentForm
	
	String email
	
	String cpf
	
	Date dueDate
	
	String description

	static constraints = {
		customerName customerName: false, size: 1..50, blank:false
		email nullable: true, blank: true, email: true, size: 1..150
		description nullable: false, size: 1..100, blank: false
		value nullable: false, blank:false, min: 5.0
		status nullable: true
		paymentForm nullable: false, blank: false
		cpf nullable: true, blank: true, size: 11..11, validator:{ value, object, errors ->
			PaymentScopes scopes = new PaymentScopes()
			Boolean cpfIsValid = scopes.validateCpf(value)
			if (!cpfIsValid) {
				return errors.rejectValue('cpf', 'CPF inválido')
			}
		}
		dueDate nullable: false, blank: false, validator:{Date value, Payment object, errors ->
			if (value.clearTime() < new Date().clearTime() && (object.isDirty('dueDate') || object.id == null)){
				return errors.rejectValue('dueDate', 'Data de vencimento inválida.')
			}	
		}
		deleted nullable: false, blank:false
	}

	static namedQueries = {
		query { Map search ->

			eq 'deleted', false

			if (search.containsKey("nameOrEmail")) {
				ilike("customerName", "%" + search.nameOrEmail + "%")
				or{
					ilike("email", "%" + search.nameOrEmail + "%")
				}
			}

			if(search.containsKey("status")){
				eq ('status', search.status)
			}

			if(search.containsKey("paymentForm")){
				eq ('paymentForm', search.paymentForm)
			}

			if(search.containsKey("initialDate")){
				ge ('dueDate', search.finalDate)
			}

			if(search.containsKey("finalDate")){
				le ('dueDate', search.finalDate)
			}
		}

		paid { Map search ->
			eq("status", PaymentStatus.PAID)

			query(search)
		}

		overduePayment { Map search ->
			eq("status", PaymentStatus.PENDING)
			le('dueDate', new Date())

			query(search)
		}
	}
}

//Para fazer o consulta será algo assim: Payment.query([nome: "marcelo almeida"]).list() ou Payment.paid([nome: "marcelo almeida"]).list()