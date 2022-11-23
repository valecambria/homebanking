let url = new URLSearchParams(window.location.search)
let urlID = url.get('id')
const app = Vue.createApp({
    data(){
        return{
            client1: [],
            clientAccountsID: [],
            clientAccounts: [],
            transferOption: [],
            sourceAccount: "",
            destinataryAccount: "",
            typeAccount: [],
            transferAmount: 0,
            transferDescription: "",
            activeAccounts: [],
        }
    },
    created(){
        this.loadData()
    },
    mounted(){

    },
    methods: {
        loadData(url){
            axios.get('/api/clients/current')
            .then((response => {
                this.client1 = response.data
                this.clientAccounts = this.client1.accounts
                this.clientAccountsID = this.clientAccounts.find(account => account.id == urlID)
                this.activeAccounts = response.data.accounts.filter(account => account.active == true)
            }))
        },
        transfer(){
            axios.post('/api/transactions', `amount=${this.transferAmount}&description=${this.transferDescription}&accountO=${this.sourceAccount}&accountD=${this.destinataryAccount}`)
            .catch(()  => {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `Please, complete all the fields to procede.`,
                })
            })   
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
                        'Transfered!',
                        'Your money has been transfered successfully.',
                        'success',
                        this.transfer()
                )
                }
            })
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