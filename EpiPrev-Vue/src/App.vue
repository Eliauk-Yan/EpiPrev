<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";

const route = useRoute();
const showLayout = computed(() => {
  return !['login', 'register'].includes(route.name as string);
});
</script>

<template>
  <div class="app-container">
    <AppHeader v-if="showLayout" />
    <main class="main-content">
      <RouterView />
    </main>
    <AppFooter v-if="showLayout" />
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  width: 100%;
  position: relative;
  /* padding and max-width removed to allow full-screen designs */
}
</style>
