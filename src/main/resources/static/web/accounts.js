const app = Vue.createApp({
    data(){
        return{
            client1: [],
            clientAccounts: [],
            clientLoans: [],
            addAccounts: [],
            accountT: "",
            accountToDelete: "",
            activeAccounts: [],
        }
    },
    created(){
        this.loadData()
    },
    mounted(){

    },
    methods: {
        ordenar(a, b){
            return a.id - b.id
        },
        loadData(){
            axios.get('/api/clients/current')
            .then((response => {
                this.client1 = response.data
                this.clientAccounts = response.data.accounts.sort(this.ordenar)   
                this.clientLoans = response.data.loans.sort(this.ordenar)  
                this.addAccounts = response.data.accounts
                this.activeAccounts = response.data.accounts.filter(account => account.active == true)
            }))
        },
        logout(){
            axios.post('/api/logout')
            .then((response =>{
                window.location.assign("./index.html")
            }))
        },
        addAccount(){
            axios.post('/api/clients/current/accounts', `accountType=${accountT}`)
            .then(() => this.loadData())
        },
        deleteAccount(){
            axios.patch('/api/clients/current/accounts/delete', `number=${this.accountToDelete}`)
            .then(()=> window.location.reload())
        },
        accountType(){
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-primary',
                    cancelButton: 'btn btn-primary'
                },
                buttonsStyling: false
            })
                swalWithBootstrapButtons.fire({
                    title: '¿What type of account wold you like to create?',
                    text: "Please, select one of the options",
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Savings',
                    cancelButtonText: 'Current',
                    reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    swalWithBootstrapButtons.fire(
                        '¡Great!',
                        'Your account was added successfully.',
                        'success',
                        accountT = 'SAVINGS',
                        this.addAccount()
                )
                } else if (
                  /* Read more about handling dismissals below */
                    result.dismiss === Swal.DismissReason.cancel
                ) {
                swalWithBootstrapButtons.fire(
                    '¡Great!',
                    'Your account was added successfully.',
                    'success',
                    accountT = 'CURRENT',
                    this.addAccount()
                )
                }
            })
        },
    },
    computed:{

    },
}).mount('#app')