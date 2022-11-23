const app = Vue.createApp({
    data(){
        return{
            client1: [],
            clientAccounts: [],
            loanOption: [],
            payments: "",
            mortgagePayments: [],
            mortgageId: 0,
            personalPayments: [],
            personalId: 0,
            vehiclePayments: [],
            vehicleId: 0,
            loanAmount: 0,
            loanDestinataryAccount: "",
            totalPayments: 0,
            activeAccounts: [],
        }
    },
    created(){
        this.loadData()
        this.getLoans()
        this.paymentsCalc()
    },
    mounted(){

    },
    methods: {
        loadData(url){
            axios.get('/api/clients/current')
            .then((response => {
                this.client1 = response.data
                this.clientAccounts = this.client1.accounts
                this.activeAccounts = response.data.accounts.filter(account => account.active == true)
            }))
        },
        getLoans(){
            axios.get('/api/loan')
            .then((response => {
                this.mortgagePayments = response.data[0].payments
                this.mortgageId = response.data[0].id
                this.personalPayments = response.data[1].payments
                this.personalId = response.data[1].id
                this.vehiclePayments = response.data[2].payments
                this.vehicleId = response.data[2].id
                console.log(response.data);
            }))
        },
        applyLoan(){
            axios.post('/api/loans', {"loanId":this.loanOption, "amount": this.loanAmount, "payments": this.payments, "destinationAccount": this.loanDestinataryAccount})
            .catch(function (error) {
                console.log(error.response.data);
                let errorData = error.response.data
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `Looks like you're ${errorData} field.`,
                })
            })  
        },
        paymentsCalc(){
            if(this.loanOption == 9){
                this.totalPayments = (this.loanAmount / this.payments) * 1.2
            }
            if(this.loanOption == 10){
                this.totalPayments = (this.loanAmount / this.payments) * 1.15
            }
            if(this.loanOption == 11){
                this.totalPayments = (this.loanAmount / this.payments) * 1.10
            }
            return this.totalPayments
        },
        confirmationAlert(){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#5bbd44',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Accepted!',
                        'Your apply was accepted successfully.',
                        'success',
                        this.applyLoan()
                )
                }
            }).then(() => window.setTimeout(function() {
                window.location.href = './accounts.html';
            }, 1500))
        },
        logout(){
            axios.post('/api/logout')
            .then((response =>{
                window.location.assign("./index.html")
            }))
        },
    },
    computed:{
    },
}).mount('#app')