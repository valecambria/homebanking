let url = new URLSearchParams(window.location.search)
let urlID = url.get('id')
const app = Vue.createApp({
    data(){
        return{
            client1: [],
            clientAccounts: [],
            accountID: [],
            accountTransactions: [],
            accountTr: [],
            accountNumber: [],
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
        loadData(url){
            axios.get('/api/clients/current')
            .then((response => {
                this.client1 = response.data
                this.clientAccounts = response.data.accounts
                this.accountID = this.clientAccounts.find(account => account.id == urlID)
                this.accountNumber = this.accountID.number
                this.accountTransactions = this.accountID.transactions.sort(this.ordenar) 
                
            }))
        },
        logout(){
            axios.post('/api/logout')
            .then((response =>{
                window.location.assign("./index.html")
            }))
        }
    },
    computed:{

    },
}).mount('#app')