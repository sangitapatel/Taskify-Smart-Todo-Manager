package com.taskify.todomanager.ui.home.parts


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.ui.categories.CategoryIcons
import com.taskify.todomanager.ui.utils.theme.gray
import com.taskify.todomanager.ui.utils.theme.primary
@Composable
fun CategoryButtonList(
    categories: List<CategoryEntity>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        item {
            CategoryChip(
                text = "All",
                iconRes = null,
                isSelected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) }
            )
        }
        items(categories) { category ->
            CategoryChip(
                text = category.name,
                iconRes = category.iconRes,
                isSelected = selectedCategoryId == category.id,
                onClick = { onCategorySelected(category.id) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    iconRes: Int?,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val borderColor = if (isSelected) primary else gray
    val iconTint = if (isSelected) primary else Color.Black
    val textColor = if (isSelected) primary else Color.Black

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(width = 1.0.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 4.dp), // slightly smaller padding
        verticalAlignment = Alignment.CenterVertically
    ) {

        val safeIcon = when {
            iconRes == null -> Icons.Default.List
            iconRes >= 0 && iconRes < CategoryIcons.iconList.size -> CategoryIcons.iconList[iconRes]
            else -> Icons.Default.Menu
        }

        Icon(
            imageVector = safeIcon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            fontSize = 12.sp, // smaller professional text
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 110.dp) // fixed width for long text
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryButtonListPreview() {
    val dummyCategories = listOf(
        CategoryEntity(1, "Work", 0),
        CategoryEntity(2, "Personal Long Name", 1),
        CategoryEntity(3, "Shopping", 2),
        CategoryEntity(4, "Study", 3),
        CategoryEntity(5, "Fitness", 4),
        CategoryEntity(6, "Very Long Category Name", 5)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        CategoryButtonList(
            categories = dummyCategories,
            selectedCategoryId = 2,
            onCategorySelected = {}
        )
    }
}
