const app = Vue.createApp({
    data(){
        return{
            client1: [],
            clientCards: [],
            debitCards: [],
            creditCards: [],
            cardToDelete: "",
            actualDate: "",
            activeCards: [],
        }
    },
    created(){
        this.loadData()
    },
    mounted(){

    },
    methods: {
        ordenar(a,b){
            return a.id - b.id
        },
        loadData(url){
            axios.get('/api/clients/current')
            .then((response => {
                this.client1 = response.data
                this.clientCards = response.data.cards.sort(this.ordenar)
                this.debitCards = response.data.cards.filter(card => card.type == 'DEBIT')
                this.creditCards = response.data.cards.filter(card => card.type == 'CREDIT')
                this.actualDate = new Date().toJSON().slice(0,10)
                this.activeCards = response.data.cards.filter(card => card.active == true)
            }))
        },
        logout(){
            axios.post('/api/logout')
            .then((response =>{
                window.location.assign("./index.html")
            }))
        },
        redirectToCards(){
            window.location.assign("./create-cards.html")
        },
        deleteCard(){
            axios.patch('/api/clients/current/cards', `number=${this.cardToDelete}`)
            .then(()=> window.location.reload())
        },
/*         currentDate(){
            const date = new Date();
            let day = date.getDate();
            let month = date.getMonth() + 1;
            let year = date.getFullYear();
        } */
    },
    computed:{

    },
}).mount('#app')