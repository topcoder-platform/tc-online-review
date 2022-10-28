<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function(){
        const activeTab = document.getElementById("activeTab");
        const tabName = document.getElementsByClassName("projectInfo__projectName")[0];
        tabName.textContent = activeTab.textContent;
    });
</script>

<div class="projectInfo">
    <h1 class="projectInfo__projectName">
    </h1>
</div>
